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

import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import org.openksavi.sponge.restapi.client.SpongeRestClient;
import org.openksavi.sponge.restapi.client.SpongeRestClientConfiguration;
import org.openksavi.sponge.restapi.client.okhttp.OkHttpSpongeRestClient;
import org.openksavi.sponge.restapi.client.util.RestClientUtils;

@Execution(ExecutionMode.SAME_THREAD)
@net.jcip.annotations.NotThreadSafe
public class OkHttpHttpsRestApiTest extends BaseHttpsRestApiTest {

    @Override
    protected SpongeRestClient createRestClient() {
        return new OkHttpSpongeRestClient(SpongeRestClientConfiguration.builder().url(String.format("https://localhost:%d", port)).build(),
                // Insecure connection only for tests.
                new OkHttpClient.Builder()
                        .sslSocketFactory(RestClientUtils.createTrustAllSslContext().getSocketFactory(),
                                RestClientUtils.createTrustAllTrustManager())
                        .hostnameVerifier((String hostname, SSLSession session) -> true).build());
    }
}
