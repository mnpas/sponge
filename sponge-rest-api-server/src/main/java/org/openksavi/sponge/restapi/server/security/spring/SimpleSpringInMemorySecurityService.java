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

package org.openksavi.sponge.restapi.server.security.spring;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.openksavi.sponge.restapi.server.RestApiIncorrectUsernamePasswordServerException;
import org.openksavi.sponge.restapi.server.security.BaseInMemoryKnowledgeBaseProvidedSecurityService;
import org.openksavi.sponge.restapi.server.security.User;

public class SimpleSpringInMemorySecurityService extends BaseInMemoryKnowledgeBaseProvidedSecurityService {

    private AuthenticationManager authenticationManager = new SimpleAuthenticationManager();

    public SimpleSpringInMemorySecurityService() {
        //
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = new User(username, password);
            authentication.getAuthorities().stream().map(a -> a.getAuthority()).forEach(user::addRole);

            return user;
        } catch (AuthenticationException e) {
            throw new RestApiIncorrectUsernamePasswordServerException("Incorrect username or password", e);
        }
    }

    class SimpleAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication auth) throws AuthenticationException {
            User user = verifyInMemory(String.valueOf(auth.getPrincipal()), String.valueOf(auth.getCredentials()));

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),
                        user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));
            }

            throw new BadCredentialsException("Incorrect username or password");
        }
    }
}
