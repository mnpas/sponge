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

package org.openksavi.sponge.core.kb;

import java.io.Reader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openksavi.sponge.EventSetProcessorState;
import org.openksavi.sponge.SpongeException;
import org.openksavi.sponge.action.ArgMeta;
import org.openksavi.sponge.action.ResultMeta;
import org.openksavi.sponge.config.Configuration;
import org.openksavi.sponge.core.engine.EngineConstants;
import org.openksavi.sponge.core.engine.GenericProcessorInstanceHolder;
import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.ProcessorInstanceHolder;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.event.EventClonePolicy;
import org.openksavi.sponge.event.EventName;
import org.openksavi.sponge.kb.KnowledgeBase;
import org.openksavi.sponge.kb.KnowledgeBaseConstants;
import org.openksavi.sponge.kb.KnowledgeBaseEngineOperations;
import org.openksavi.sponge.kb.KnowledgeBaseScript;
import org.openksavi.sponge.kb.KnowledgeBaseType;
import org.openksavi.sponge.kb.ScriptKnowledgeBaseInterpreter;
import org.openksavi.sponge.rule.EventMode;

/**
 * Script based knowledge base interpreter.
 */
public abstract class BaseScriptKnowledgeBaseInterpreter extends BaseKnowledgeBaseInterpreter implements ScriptKnowledgeBaseInterpreter {

    private static final Logger logger = LoggerFactory.getLogger(BaseScriptKnowledgeBaseInterpreter.class);

    public static final String PROP_PATH_SEPARATOR = ", \t";

    /** Synchronization processor. */
    protected Object interpteterSynchro = new Object();

    @SuppressWarnings("rawtypes")
    protected ScriptClassInstanceProvider scriptClassInstancePovider;

    /**
     * Creates a new knowledge base interpreter.
     *
     * @param engineOperations an engine operations.
     * @param type a knowledge base type.
     */
    protected BaseScriptKnowledgeBaseInterpreter(KnowledgeBaseEngineOperations engineOperations, KnowledgeBaseType type) {
        super(engineOperations, type);

        prepareInterpreter();

        scriptClassInstancePovider = createScriptClassInstancePovider();
    }

    /**
     * Prepares the interpreter.
     */
    protected abstract void prepareInterpreter();

    protected abstract <T> ScriptClassInstanceProvider<T> createScriptClassInstancePovider();

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T doCreateInstance(String className, Class<T> javaClass) {
        return (T) scriptClassInstancePovider.newInstance(className, javaClass);
    }

    protected final void invalidateCache() {
        scriptClassInstancePovider.clear();
    }

    @Override
    public final void load(List<KnowledgeBaseScript> scripts) {
        if (scripts.size() > 1) {
            logger.debug("Loading knowledge base '{}' from {}.", scripts.get(0).getKnowledgeBase().getName(),
                    scripts.stream().map(script -> script.getName()).collect(Collectors.joining(", ", "'", "'")));
        }
        synchronized (interpteterSynchro) {
            scripts.forEach(script -> loadKnowledgeBaseScript(script));
        }
    }

    /**
     * Loads the knowledge base from the file.
     *
     * @param fileName file name.
     */
    @Override
    public final void load(String fileName) {
        load(fileName, Charset.defaultCharset());
    }

    /**
     * Loads the knowledge base from the file.
     *
     * @param fileName file name.
     * @param charset charset.
     */
    @Override
    public final void load(String fileName, String charset) {
        load(fileName, Charset.forName(charset));
    }

    @Override
    public final void load(String fileName, Charset charset) {
        load(fileName, charset, true);
    }

    @Override
    public final void load(String fileName, Charset charset, boolean required) {
        load(new FileKnowledgeBaseScript(fileName, charset, required));
    }

    @Override
    public final void load(KnowledgeBaseScript script) {
        synchronized (interpteterSynchro) {
            invalidateCache();

            SpongeEngine engine = getEngineOperations().getEngine();

            try (Reader reader = engine.getKnowledgeBaseManager().getKnowledgeBaseScriptProvider(script).getReader()) {
                if (reader != null) {
                    doLoad(reader, script.getName());
                }
            } catch (Throwable e) {
                throw SpongeUtils.wrapException(script.getName(), this, e);
            }
        }
    }

    protected void doLoad(Reader reader, String name) {
        eval(reader, name);
    }

    @Override
    public final void reload(List<KnowledgeBaseScript> scripts) {
        synchronized (interpteterSynchro) {
            doReload(scripts);
        }
    }

    protected void doReload(List<KnowledgeBaseScript> scripts) {
        load(scripts);
    }

    private void loadKnowledgeBaseScript(KnowledgeBaseScript script) {
        logger.info("Loading knowledge base '{}' from {}.", script.getKnowledgeBase().getName(), script.getName());

        load(script);
    }

    @Override
    public void onInit() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_INIT, null);
    }

    @Override
    public void onLoad() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_LOAD, null);
    }

    /**
     * Calls onStartup method in the knowledge base.
     */
    @Override
    public void onStartup() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_STARTUP, null);
    }

    @Override
    public boolean onRun() {
        Object result = invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_RUN, null);
        if (result != null) {
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                throw new SpongeException(
                        "The knowledge base onRun callback function should return a boolean value, not: " + result.getClass());
            }
        }

        return EngineConstants.DEFAULT_ON_RUN_FUNCTION_RESULT;
    }

    /**
     * Calls onShutdown method in the knowledge base.
     */
    @Override
    public void onShutdown() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_SHUTDOWN, null);
    }

    /**
     * Calls onBeforeReload method in the knowledge base.
     */
    @Override
    public void onBeforeReload() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_BEFORE_RELOAD, null);
    }

    /**
     * Calls onAfterReload method in the knowledge base.
     */
    @Override
    public void onAfterReload() {
        invokeOptionalFunction(KnowledgeBaseConstants.FUN_ON_AFTER_RELOAD, null);
    }

    @Override
    public <T> T eval(String expression, Class<T> cls) {
        return eval(expression);
    }

    public abstract <T> T eval(Reader reader, String fileName);

    protected abstract ScriptKnowledgeBaseInterpreter createInterpreterInstance(SpongeEngine engine, KnowledgeBase knowledgeBase);

    protected List<Class<?>> getStandardImportClasses() {
        List<Class<?>> classes = new ArrayList<>();
        //@formatter:off
        classes.addAll(Arrays.asList(EventMode.class, EventClonePolicy.class, SpongeUtils.class, SpongeException.class,
                Event.class, Configuration.class, EventSetProcessorState.class, EventName.class,
                ArgMeta.class, ResultMeta.class,
                Duration.class, Instant.class, ChronoUnit.class, TimeUnit.class));
        //@formatter:on

        classes.addAll(SpongeUtils.getSupportedTypes());

        return classes;
    }

    protected boolean isProcessorAbstract(String className) {
        return className.startsWith(KnowledgeBaseConstants.ABSTRACT_PROCESSOR_CLASS_NAME_PREFIX);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ProcessorInstanceHolder createProcessorInstanceByProcessorClass(KnowledgeBase knowledgeBase, Object processorClass,
            Class<?> javaClass) {
        String name = knowledgeBase.getInterpreter().getScriptKnowledgeBaseProcessorClassName(processorClass);

        return name != null ? new GenericProcessorInstanceHolder(createProcessorInstance(name, (Class) javaClass), name, false) : null;
    }
}
