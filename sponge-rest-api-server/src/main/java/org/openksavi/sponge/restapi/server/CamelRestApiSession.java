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

package org.openksavi.sponge.restapi.server;

import org.apache.camel.Exchange;

import org.openksavi.sponge.restapi.server.security.User;

/**
 * A Camel-based REST API session.
 */
public class CamelRestApiSession extends DefaultRestApiSession {

    private Exchange exchange;

    public CamelRestApiSession(User user, Exchange exchange) {
        super(user);
        this.exchange = exchange;
    }

    public Exchange getExchange() {
        return exchange;
    }
}
