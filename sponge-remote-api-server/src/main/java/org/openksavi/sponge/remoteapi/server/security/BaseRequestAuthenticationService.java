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

package org.openksavi.sponge.remoteapi.server.security;

import org.openksavi.sponge.remoteapi.server.RemoteApiService;

public abstract class BaseRequestAuthenticationService implements RequestAuthenticationService {

    private RemoteApiService remoteApiService;

    protected BaseRequestAuthenticationService() {
        //
    }

    @Override
    public RemoteApiService getRemoteApiService() {
        return remoteApiService;
    }

    @Override
    public void setRemoteApiService(RemoteApiService remoteApiService) {
        this.remoteApiService = remoteApiService;
    }

    @Override
    public void init() {
        //
    }

    @Override
    public void dispose() {
        //
    }
}
