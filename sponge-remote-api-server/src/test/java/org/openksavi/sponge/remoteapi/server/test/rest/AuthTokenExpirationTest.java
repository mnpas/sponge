/*
 * Copyright 2016-2018 The Sponge authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openksavi.sponge.remoteapi.server.test.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.grpcapi.server.GrpcApiServerPlugin;
import org.openksavi.sponge.remoteapi.server.test.PortTestConfig;
import org.openksavi.sponge.restapi.client.DefaultSpongeRestClient;
import org.openksavi.sponge.restapi.client.InvalidAuthTokenException;
import org.openksavi.sponge.restapi.client.SpongeRestClient;
import org.openksavi.sponge.restapi.client.SpongeRestClientConfiguration;
import org.openksavi.sponge.restapi.server.RestApiServerPlugin;
import org.openksavi.sponge.restapi.server.security.RestApiSecurityService;
import org.openksavi.sponge.restapi.server.security.spring.SimpleSpringInMemorySecurityService;
import org.openksavi.sponge.spring.SpringSpongeEngine;

@Execution(ExecutionMode.SAME_THREAD)
@net.jcip.annotations.NotThreadSafe
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AuthTokenExpirationTest.TestConfig.class })
@DirtiesContext
public class AuthTokenExpirationTest {

    @Inject
    protected SpongeEngine engine;

    @Inject
    @Named(PortTestConfig.PORT_BEAN_NAME)
    protected Integer port;

    @Configuration
    public static class TestConfig extends PortTestConfig {

        @Bean
        public SpongeEngine spongeEngine() {
            return SpringSpongeEngine.builder().plugins(camelPlugin(), spongeRestApiPlugin(), spongeGrpcApiPlugin())
                    .config("examples/remote-api-server/rest_api_security.xml").build();
        }

        @Bean
        public RestApiServerPlugin spongeRestApiPlugin() {
            RestApiServerPlugin plugin = new RestApiServerPlugin();

            plugin.getSettings().setPort(spongeRestApiPort());
            plugin.getSettings().setAllowAnonymous(false);
            plugin.getSettings().setAuthTokenExpirationDuration(Duration.ofSeconds(2));

            plugin.setSecurityService(restApiSecurityService());

            return plugin;
        }

        @Bean
        public GrpcApiServerPlugin spongeGrpcApiPlugin() {
            return new GrpcApiServerPlugin();
        }

        @Bean
        public RestApiSecurityService restApiSecurityService() {
            return new SimpleSpringInMemorySecurityService();
        }
    }

    protected SpongeRestClient createRestClient(String username, String password) {
        return new DefaultSpongeRestClient(SpongeRestClientConfiguration.builder().url(String.format("http://localhost:%d", port))
                .username(username).password(password).build());
    }

    @Test
    public void testAuthTokeExpirationRelogin() throws InterruptedException {
        try (SpongeRestClient client = createRestClient("john", "password")) {
            client.getConfiguration().setRelogin(true);
            assertNotNull(client.login());
            assertEquals(RestApiTestConstants.ADMIN_ACTIONS_COUNT, client.getActions().size());

            TimeUnit.SECONDS.sleep(3);

            assertEquals(RestApiTestConstants.ADMIN_ACTIONS_COUNT, client.getActions().size());
        }
    }

    @Test
    public void testAuthTokeExpirationNoRelogin() throws InterruptedException {
        try (SpongeRestClient client = createRestClient("john", "password")) {
            client.getConfiguration().setRelogin(false);
            assertNotNull(client.login());
            assertEquals(RestApiTestConstants.ADMIN_ACTIONS_COUNT, client.getActions().size());

            TimeUnit.SECONDS.sleep(3);

            assertThrows(InvalidAuthTokenException.class, () -> client.getActions());
        }
    }
}
