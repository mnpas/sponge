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

package org.openksavi.sponge.restapi.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.restapi.RestApiConstants;
import org.openksavi.sponge.restapi.client.DefaultSpongeRestApiClient;
import org.openksavi.sponge.restapi.client.InvalidAuthTokenException;
import org.openksavi.sponge.restapi.client.RestApiClientConfiguration;
import org.openksavi.sponge.restapi.client.SpongeRestApiClient;
import org.openksavi.sponge.restapi.server.RestApiServerPlugin;
import org.openksavi.sponge.restapi.server.security.RestApiSecurityService;
import org.openksavi.sponge.restapi.server.security.spring.SimpleSpringInMemorySecurityService;
import org.openksavi.sponge.spring.SpringSpongeEngine;

@net.jcip.annotations.NotThreadSafe
@RunWith(CamelSpringRunner.class)
@ContextConfiguration(classes = { AuthTokenExpirationTest.TestConfig.class }, loader = CamelSpringDelegatingTestContextLoader.class)
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
            return SpringSpongeEngine.builder().plugins(camelPlugin(), spongeRestApiPlugin())
                    .knowledgeBase("admin", "classpath:org/openksavi/sponge/restapi/server/administration_library.py")
                    .knowledgeBase("example", "examples/rest-api-server/rest_api.py")
                    .knowledgeBase("security", "examples/rest-api-server/rest_api_security.py").build();
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
        public RestApiSecurityService restApiSecurityService() {
            return new SimpleSpringInMemorySecurityService();
        }
    }

    protected SpongeRestApiClient createRestApiClient(String username, String password) {
        return new DefaultSpongeRestApiClient(
                RestApiClientConfiguration.builder().url(String.format("http://localhost:%d/%s", port, RestApiConstants.DEFAULT_PATH))
                        .username(username).password(password).build());
    }

    @Test
    public void testAuthTokeExpirationRelogin() throws InterruptedException {
        try (SpongeRestApiClient client = createRestApiClient("john", "password")) {
            client.getConfiguration().setRelogin(true);
            assertNotNull(client.login());
            assertEquals(RestApiTestConstants.ALL_ACTION_COUNT, client.getActions().size());

            TimeUnit.SECONDS.sleep(3);

            assertEquals(RestApiTestConstants.ALL_ACTION_COUNT, client.getActions().size());
        }
    }

    @Test(expected = InvalidAuthTokenException.class)
    public void testAuthTokeExpirationNoRelogin() throws InterruptedException {
        try (SpongeRestApiClient client = createRestApiClient("john", "password")) {
            client.getConfiguration().setRelogin(false);
            assertNotNull(client.login());
            assertEquals(RestApiTestConstants.ALL_ACTION_COUNT, client.getActions().size());

            TimeUnit.SECONDS.sleep(3);

            client.getActions();
        }
    }
}
