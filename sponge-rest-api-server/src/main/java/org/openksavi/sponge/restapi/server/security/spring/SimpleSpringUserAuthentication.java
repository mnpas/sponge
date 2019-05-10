/*
 * Copyright 2016-2019 The Sponge authors.
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

package org.openksavi.sponge.restapi.server.security.spring;

import org.springframework.security.core.Authentication;

import org.openksavi.sponge.restapi.server.security.User;
import org.openksavi.sponge.restapi.server.security.UserAuthentication;

public class SimpleSpringUserAuthentication extends UserAuthentication {

    private Authentication authentication;

    public SimpleSpringUserAuthentication(User user, Authentication authentication) {
        super(user);

        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}