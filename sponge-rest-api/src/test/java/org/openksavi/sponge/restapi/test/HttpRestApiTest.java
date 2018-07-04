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

package org.openksavi.sponge.restapi.test;

import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import org.openksavi.sponge.camel.SpongeCamelConfiguration;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.restapi.RestApiPlugin;
import org.openksavi.sponge.spring.SpringSpongeEngine;

@RunWith(CamelSpringRunner.class)
@ContextConfiguration(classes = { HttpRestApiTest.TestConfig.class }, loader = CamelSpringDelegatingTestContextLoader.class)
@DirtiesContext
public class HttpRestApiTest extends BaseRestApiTestTemplate {

    @Configuration
    public static class TestConfig extends SpongeCamelConfiguration {

        @Bean
        public SpongeEngine spongeEngine() {
            return SpringSpongeEngine.builder().plugins(camelPlugin(), spongeRestApiPlugin())
                    .knowledgeBase("kb", "examples/rest-api/rest_api.py").build();
        }

        @Bean
        public RestApiPlugin spongeRestApiPlugin() {
            RestApiPlugin plugin = new RestApiPlugin();

            plugin.getSettings().setPort(PORT);
            // plugin.getSettings().setPublicActions(Arrays.asList(new ProcessorQualifiedName(".*", "^(?!)Private.*")));

            return plugin;
        }
    }

    @Override
    protected String getProtocol() {
        return "http";
    }

    @Override
    protected RestTemplate createRestTemplate() {
        return setupRestTemplate(new RestTemplate());
    }
}