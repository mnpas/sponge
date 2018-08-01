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

import java.time.Duration;
import java.util.List;

import org.openksavi.sponge.ProcessorQualifiedName;
import org.openksavi.sponge.core.util.SslConfiguration;
import org.openksavi.sponge.restapi.RestApiConstants;

/**
 * REST API settings.
 */
public class RestApiSettings {

    /** The Camel REST component id. */
    private String restComponentId = RestApiServerConstants.DEFAULT_REST_COMPONENT_ID;

    /** The host. */
    private String host;

    /** The port. */
    private Integer port = RestApiConstants.DEFAULT_PORT;

    /** The API version. */
    private int version = RestApiConstants.API_VERSION;

    /** The pretty print option. */
    private boolean prettyPrint = RestApiServerConstants.DEFAULT_PRETTY_PRINT;

    /** Public actions. */
    private List<ProcessorQualifiedName> publicActions;

    /** Public event names. */
    private List<String> publicEvents;

    /** The SSLContextParameters bean name. */
    private String sslContextParametersBeanName = RestApiServerConstants.DEFAULT_SSL_CONTEXT_PARAMETERS_BEAN_NAME;

    /** The SSL configuration. */
    private SslConfiguration sslConfiguration;

    /** If {@code true} then the reload operation will be published. The default value is {@code true}. */
    private boolean publishReload = RestApiServerConstants.DEFAULT_PUBLISH_RELOAD;

    private boolean allowAnonymous = RestApiServerConstants.DEFAULT_ALLOW_ANONYMOUS;

    private String adminRole = RestApiServerConstants.DEFAULT_ROLE_ADMIN;

    private String guestRole = RestApiServerConstants.DEFAULT_ROLE_GUEST;

    private boolean includeDetailedErrorMessage = RestApiServerConstants.DEFAULT_INCLUDE_DETAILED_ERROR_MESSAGE;

    /** The duration after which an authentication token will expire. The default value is {@code null} which means infinity. */
    private Duration authTokenExpirationDuration;

    public String getRestComponentId() {
        return restComponentId;
    }

    public void setRestComponentId(String restComponentId) {
        this.restComponentId = restComponentId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public List<ProcessorQualifiedName> getPublicActions() {
        return publicActions;
    }

    public void setPublicActions(List<ProcessorQualifiedName> publicActions) {
        this.publicActions = publicActions;
    }

    public List<String> getPublicEvents() {
        return publicEvents;
    }

    public void setPublicEvents(List<String> publicEvents) {
        this.publicEvents = publicEvents;
    }

    public String getSslContextParametersBeanName() {
        return sslContextParametersBeanName;
    }

    public void setSslContextParametersBeanName(String sslContextParametersBeanName) {
        this.sslContextParametersBeanName = sslContextParametersBeanName;
    }

    public SslConfiguration getSslConfiguration() {
        return sslConfiguration;
    }

    public void setSslConfiguration(SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    public boolean isPublishReload() {
        return publishReload;
    }

    public void setPublishReload(boolean publishReload) {
        this.publishReload = publishReload;
    }

    public boolean isAllowAnonymous() {
        return allowAnonymous;
    }

    public void setAllowAnonymous(boolean allowAnonymous) {
        this.allowAnonymous = allowAnonymous;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    public String getGuestRole() {
        return guestRole;
    }

    public void setGuestRole(String guestRole) {
        this.guestRole = guestRole;
    }

    public boolean isIncludeDetailedErrorMessage() {
        return includeDetailedErrorMessage;
    }

    public void setIncludeDetailedErrorMessage(boolean includeDetailedErrorMessage) {
        this.includeDetailedErrorMessage = includeDetailedErrorMessage;
    }

    public Duration getAuthTokenExpirationDuration() {
        return authTokenExpirationDuration;
    }

    public void setAuthTokenExpirationDuration(Duration authTokenExpirationDuration) {
        this.authTokenExpirationDuration = authTokenExpirationDuration;
    }
}