/*
 * Copyright 2016-2017 The Sponge authors.
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

package org.openksavi.sponge.camel;

import org.apache.camel.Exchange;
import org.apache.camel.language.xpath.XPathBuilder;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;

import org.openksavi.sponge.core.util.SslConfiguration;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.event.EventDefinition;

/**
 * Camel integration utility methods.
 */
public abstract class CamelUtils {

    public static Event getEvent(Exchange exchange) {
        if (exchange.getIn() != null) {
            Object body = exchange.getIn().getBody();
            if (body != null) {
                if (body instanceof Event) {
                    return (Event) body;
                } else if (body instanceof EventDefinition) {
                    return ((EventDefinition) body).make();
                }
            }
        }

        return null;
    }

    public static Event getOrCreateInputEvent(SpongeEngine engine, Exchange exchange) {
        Event event = getEvent(exchange);

        if (event == null) {
            event = SpongeCamelEvent.create(engine, exchange);
        }

        return event;
    }

    public static String xpath(Exchange exchange, String path) {
        return XPathBuilder.xpath(path).stringResult().evaluate(exchange, String.class);
    }

    public static CamelPlugin getPlugin(SpongeEngine engine) {
        return engine.getOperations().getPlugin(CamelPlugin.class, CamelPlugin.NAME);
    }

    public static boolean hasPlugin(SpongeEngine engine) {
        return engine.getOperations().hasPlugin(CamelPlugin.class, CamelPlugin.NAME);
    }

    public static SSLContextParameters createSslContextParameters(SslConfiguration security) {
        KeyStoreParameters keyStoreParameters = new KeyStoreParameters();
        keyStoreParameters.setResource(security.getKeyStore());
        keyStoreParameters.setPassword(security.getKeyStorePassword());

        KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
        keyManagersParameters.setKeyStore(keyStoreParameters);
        keyManagersParameters.setKeyPassword(security.getKeyPassword());

        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        trustManagersParameters.setKeyStore(keyStoreParameters);

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(keyManagersParameters);
        sslContextParameters.setTrustManagers(trustManagersParameters);

        return sslContextParameters;
    }
}
