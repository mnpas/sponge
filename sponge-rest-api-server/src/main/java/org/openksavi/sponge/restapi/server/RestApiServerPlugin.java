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

package org.openksavi.sponge.restapi.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

import org.openksavi.sponge.camel.CamelPlugin;
import org.openksavi.sponge.camel.CamelUtils;
import org.openksavi.sponge.config.ConfigException;
import org.openksavi.sponge.config.Configuration;
import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.java.JPlugin;
import org.openksavi.sponge.restapi.server.security.NoSecuritySecurityService;
import org.openksavi.sponge.restapi.server.security.RestApiAuthTokenService;
import org.openksavi.sponge.restapi.server.security.RestApiSecurityService;
import org.openksavi.sponge.restapi.server.security.User;
import org.openksavi.sponge.restapi.server.util.RestApServeriUtils;

/**
 * Sponge REST API server plugin.
 */
public class RestApiServerPlugin extends JPlugin {

    public static final String NAME = "restApiServer";

    private static final Supplier<RestApiService> DEFAULT_API_SERVICE_PROVIDER = () -> new DefaultRestApiService();

    private static final Supplier<RestApiSecurityService> DEFAULT_SECURITY_SERVICE_PROVIDER = () -> new NoSecuritySecurityService();

    private static final Supplier<RestApiRouteBuilder> DEFAULT_ROUTE_BUILDER_PROVIDER = () -> new RestApiRouteBuilder();

    private RestApiSettings settings = new RestApiSettings();

    /** If {@code true} then the REST service will start when the plugin starts up. The default value is {@code true}. */
    private boolean autoStart = RestApiServerConstants.DEFAULT_AUTO_START;

    private AtomicBoolean started = new AtomicBoolean(false);

    private RestApiRouteBuilder routeBuilder;

    private RestApiService service;

    private RestApiSecurityService securityService;

    private RestApiAuthTokenService authTokenService;

    private CamelContext camelContext;

    private Lock lock = new ReentrantLock(true);

    public RestApiServerPlugin() {
        setName(NAME);
    }

    @Override
    public void onConfigure(Configuration configuration) {
        settings.setRestComponentId(configuration.getString(RestApiServerConstants.TAG_REST_COMPONENT_ID, settings.getRestComponentId()));
        settings.setHost(configuration.getString(RestApiServerConstants.TAG_HOST, settings.getHost()));
        settings.setPort(configuration.getInteger(RestApiServerConstants.TAG_PORT, settings.getPort()));
        settings.setPrettyPrint(configuration.getBoolean(RestApiServerConstants.TAG_PRETTY_PRINT, settings.isPrettyPrint()));

        String publicActionsSpec = configuration.getString(RestApiServerConstants.TAG_PUBLIC_ACTIONS, null);
        if (publicActionsSpec != null) {
            settings.setPublicActions(SpongeUtils.getProcessorQualifiedNameList(publicActionsSpec));
        }

        String publicEvents = configuration.getString(RestApiServerConstants.TAG_PUBLIC_EVENTS, null);
        if (publicEvents != null) {
            settings.setPublicEvents(SpongeUtils.getNameList(publicEvents));
        }

        if (configuration.hasChildConfiguration(RestApiServerConstants.TAG_SSL_CONFIGURATION)) {
            settings.setSslConfiguration(
                    SpongeUtils.createSslConfiguration(configuration.getChildConfiguration(RestApiServerConstants.TAG_SSL_CONFIGURATION)));
        }

        settings.setPublishReload(configuration.getBoolean(RestApiServerConstants.TAG_PUBLISH_RELOAD, settings.isPublishReload()));
        settings.setAllowAnonymous(configuration.getBoolean(RestApiServerConstants.TAG_ALLOW_ANONYMOUS, settings.isAllowAnonymous()));

        autoStart = configuration.getBoolean(RestApiServerConstants.TAG_AUTO_START, isAutoStart());

        String routeBuilderClass = configuration.getString(RestApiServerConstants.TAG_ROUTE_BUILDER_CLASS, null);
        if (routeBuilderClass != null) {
            routeBuilder = SpongeUtils.createInstance(routeBuilderClass, RestApiRouteBuilder.class);
        }

        String apiServiceClass = configuration.getString(RestApiServerConstants.TAG_API_SERVICE_CLASS, null);
        if (apiServiceClass != null) {
            service = SpongeUtils.createInstance(apiServiceClass, RestApiService.class);
        }

        String securityServiceClass = configuration.getString(RestApiServerConstants.TAG_SECURITY_SERVICE_CLASS, null);
        if (securityServiceClass != null) {
            securityService = SpongeUtils.createInstance(securityServiceClass, RestApiSecurityService.class);
        }

        String authTokenServiceClass = configuration.getString(RestApiServerConstants.TAG_AUTH_TOKEN_SERVICE_CLASS, null);
        if (authTokenServiceClass != null) {
            authTokenService = SpongeUtils.createInstance(authTokenServiceClass, RestApiAuthTokenService.class);
        }
    }

    @Override
    public void onStartup() {
        if (isAutoStart()) {
            start();
        }
    }

    public RestApiServerPlugin(String name) {
        super(name);
    }

    public RestApiSettings getSettings() {
        return settings;
    }

    public void start() {
        CamelContext finalCamelContext = camelContext;
        if (finalCamelContext == null) {
            CamelPlugin camelPlugin = getEngine().getPluginManager().getPlugin(CamelPlugin.class);
            if (camelPlugin == null) {
                throw new ConfigException(
                        "Camel plugin is not registered but it is required by the Sponge REST API if no Camel context is set");
            }

            finalCamelContext = camelPlugin.getCamelContext();
        }

        start(finalCamelContext);
    }

    public void start(CamelContext camelContext) {
        if (camelContext == null) {
            throw new ConfigException("Camel context is not available");
        }

        lock.lock();

        try {
            if (!started.get()) {
                try {
                    if (settings.getSslConfiguration() != null && settings.getSslContextParametersBeanName() != null) {
                        setupSecurity(camelContext);
                    }

                    if (service == null) {
                        // Create a default.
                        service = DEFAULT_API_SERVICE_PROVIDER.get();
                    }
                    service.setSettings(settings);
                    service.setEngine(getEngine());

                    if (securityService == null) {
                        // Create a default.
                        securityService = DEFAULT_SECURITY_SERVICE_PROVIDER.get();
                    }

                    securityService.setEngine(getEngine());
                    service.setSecurityService(securityService);

                    // No default auth token service is used, only null.
                    if (authTokenService != null) {
                        authTokenService.setEngine(getEngine());
                        service.setAuthTokenService(authTokenService);
                    }

                    if (routeBuilder == null) {
                        // Create a default.
                        routeBuilder = DEFAULT_ROUTE_BUILDER_PROVIDER.get();
                    }
                    routeBuilder.setSettings(settings);
                    routeBuilder.setApiService(service);

                    camelContext.addRoutes(routeBuilder);
                } catch (Exception e) {
                    throw SpongeUtils.wrapException(e);
                }

                started.set(true);
            }
        } finally {
            lock.unlock();
        }
    }

    protected void setupSecurity(CamelContext camelContext) {
        SimpleRegistry simpleRegistry = new SimpleRegistry();
        simpleRegistry.put(settings.getSslContextParametersBeanName(),
                CamelUtils.createSslContextParameters(settings.getSslConfiguration()));

        // TODO Handle many invocations of this method resulting in a growing registry list.
        ((DefaultCamelContext) camelContext).setRegistry(new CompositeRegistry(Arrays.asList(simpleRegistry, camelContext.getRegistry())));
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public RestApiRouteBuilder getRouteBuilder() {
        return routeBuilder;
    }

    public void setRouteBuilder(RestApiRouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
    }

    public RestApiService getService() {
        return service;
    }

    public void setService(RestApiService service) {
        this.service = service;
    }

    public RestApiSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(RestApiSecurityService securityService) {
        this.securityService = securityService;
    }

    public RestApiAuthTokenService getAuthTokenService() {
        return authTokenService;
    }

    public void setAuthTokenService(RestApiAuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    public boolean canUseKnowledgeBase(Map<String, Collection<String>> roleToKnowledgeBases, User user, String kbName) {
        return RestApServeriUtils.canUseKnowledgeBase(roleToKnowledgeBases, user, kbName);
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}
