/*
 * Copyright 2016-2020 The Sponge authors.
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

package org.openksavi.sponge.remoteapi.server.security.spring;

import org.openksavi.sponge.remoteapi.server.security.DefaultRequestAuthenticationService;
import org.openksavi.sponge.remoteapi.server.security.KnowledgeBaseProvidedAccessService;
import org.openksavi.sponge.remoteapi.server.security.AccessService;
import org.openksavi.sponge.remoteapi.server.security.SecurityProvider;
import org.openksavi.sponge.remoteapi.server.security.SecurityService;
import org.openksavi.sponge.remoteapi.server.security.RequestAuthenticationService;

public class SimpleSpringInMemorySecurityProvider implements SecurityProvider {

    @Override
    public SecurityService createSecurityService() {
        return new SimpleSpringInMemorySecurityService();
    }

    @Override
    public AccessService createAccessService() {
        return new KnowledgeBaseProvidedAccessService();
    }

    @Override
    public RequestAuthenticationService createRequestAuthenticationService() {
        return new DefaultRequestAuthenticationService();
    }
}
