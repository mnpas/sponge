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

package org.openksavi.sponge.core.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.ExceptionContext;
import org.openksavi.sponge.engine.ExceptionHandler;

/**
 * Logging exception handler.
 */
public class LoggingExceptionHandler implements ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoggingExceptionHandler.class);

    private boolean includeStackTrace;

    public LoggingExceptionHandler(boolean includeStackTrace) {
        this.includeStackTrace = includeStackTrace;
    }

    public LoggingExceptionHandler() {
        this(true);
    }

    public boolean isIncludeStackTrace() {
        return includeStackTrace;
    }

    public void setIncludeStackTrace(boolean includeStackTrace) {
        this.includeStackTrace = includeStackTrace;
    }

    @Override
    public void handleException(Throwable exception, ExceptionContext context) {
        logger.error(SpongeUtils.createErrorMessage(exception, context, includeStackTrace));
    }
}
