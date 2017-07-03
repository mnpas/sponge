/*
 * Copyright 2016-2017 Softelnet.
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

import org.openksavi.sponge.engine.EngineParameters;
import org.openksavi.sponge.event.EventClonePolicy;

/**
 * Default values of engine parameters.
 */
public class DefaultEngineParameters implements EngineParameters {

    /** Default number of the Main Processing Unit worker threads. */
    private int mainProcessingUnitThreadCount = 5;

    /** Default number of threads used by an event set processor asynchronous executor. */
    private int asyncEventSetProcessorExecutorThreadCount = 5;

    /** Default number of threads used by processing units to listen to an event queue concurrently. */
    private int processingUnitConcurrentListenerThreadCount = 1;

    /** Default event queue capacity. */
    private int eventQueueCapacity = -1;

    /** Default Event Scheduler thread count. */
    private int eventSchedulerThreadCount = 1;

    /** Default duration thread count. */
    private int durationThreadCount = 1;

    /** Default event clone policy. */
    private EventClonePolicy eventClonePolicy = EventClonePolicy.SHALLOW;

    /** Default event set processor synchronous flag. */
    private boolean eventSetProcessorDefaultSynchronous = false;

    /** Auto-enable processors. */
    private boolean autoEnable = true;

    /** Executor shutdown timeout (in seconds). */
    private long executorShutdownTimeout = 20;

    /**
     * A specific {link org.openksavi.sponge.engine.processing.EventSetProcessorMainProcessingUnitHandler} implementation parameter - an
     * asynchronous processing partition size.
     */
    private int asyncEventSetProcessorProcessingPartitionSize = 1000;

    private int asyncEventSetProcessorProcessingThreshold = 1;

    /** A flag to allow for concurrent processing of events that have the same type by event set processors. */
    private boolean allowConcurrentEventTypeProcessingByEventSetProcessors = true;

    /** A main Processing Unit worker executor queue size. */
    private int mainProcessingUnitWorkerExecutorQueueSize = 100;

    /** An internal queue blocking put operation sleep time between retries (in milliseconds). */
    private long internalQueueBlockingPutSleep = 10;

    /** A decomposed queue capacity. */
    private int decomposedQueueCapacity = 100;

    /** A Main Event Queue capacity. */
    private int mainEventQueueCapacity = 10;

    /** A dynamic thread pool initial size ratio. */
    private double initialDynamicThreadPoolSizeRatio = 0.3;

    /** A dynamic thread pool keep alive time (in milliseconds). */
    private long dynamicThreadPoolKeepAliveTime = 60000;

    @Override
    public int getMainProcessingUnitThreadCount() {
        return mainProcessingUnitThreadCount;
    }

    @Override
    public int getProcessingUnitConcurrentListenerThreadCount() {
        return processingUnitConcurrentListenerThreadCount;
    }

    @Override
    public int getEventQueueCapacity() {
        return eventQueueCapacity;
    }

    @Override
    public int getEventSchedulerThreadCount() {
        return eventSchedulerThreadCount;
    }

    @Override
    public int getDurationThreadCount() {
        return durationThreadCount;
    }

    @Override
    public int getAsyncEventSetProcessorExecutorThreadCount() {
        return asyncEventSetProcessorExecutorThreadCount;
    }

    @Override
    public EventClonePolicy getEventClonePolicy() {
        return eventClonePolicy;
    }

    @Override
    public boolean getEventSetProcessorDefaultSynchronous() {
        return eventSetProcessorDefaultSynchronous;
    }

    @Override
    public long getExecutorShutdownTimeout() {
        return executorShutdownTimeout;
    }

    @Override
    public boolean getAllowConcurrentEventTypeProcessingByEventSetProcessors() {
        return allowConcurrentEventTypeProcessingByEventSetProcessors;
    }

    @Override
    public int getAsyncEventSetProcessorProcessingPartitionSize() {
        return asyncEventSetProcessorProcessingPartitionSize;
    }

    @Override
    public boolean getAutoEnable() {
        return autoEnable;
    }

    @Override
    public void setMainProcessingUnitThreadCount(int mainProcessingUnitThreadCount) {
        this.mainProcessingUnitThreadCount = mainProcessingUnitThreadCount;
    }

    @Override
    public void setProcessingUnitConcurrentListenerThreadCount(int processingUnitConcurrentListenerThreadCount) {
        this.processingUnitConcurrentListenerThreadCount = processingUnitConcurrentListenerThreadCount;
    }

    @Override
    public void setEventQueueCapacity(int eventQueueCapacity) {
        this.eventQueueCapacity = eventQueueCapacity;
    }

    @Override
    public void setEventSchedulerThreadCount(int eventSchedulerThreadCount) {
        this.eventSchedulerThreadCount = eventSchedulerThreadCount;
    }

    @Override
    public void setDurationThreadCount(int durationThreadCount) {
        this.durationThreadCount = durationThreadCount;
    }

    @Override
    public void setAsyncEventSetProcessorExecutorThreadCount(int asyncEventSetProcessorExecutorThreadCount) {
        this.asyncEventSetProcessorExecutorThreadCount = asyncEventSetProcessorExecutorThreadCount;
    }

    @Override
    public void setEventClonePolicy(EventClonePolicy eventClonePolicy) {
        this.eventClonePolicy = eventClonePolicy;
    }

    @Override
    public void setEventSetProcessorDefaultSynchronous(boolean eventSetProcessorDefaultSynchronous) {
        this.eventSetProcessorDefaultSynchronous = eventSetProcessorDefaultSynchronous;
    }

    @Override
    public void setExecutorShutdownTimeout(long executorShutdownTimeout) {
        this.executorShutdownTimeout = executorShutdownTimeout;
    }

    @Override
    public void setAsyncEventSetProcessorProcessingPartitionSize(int asyncEventSetProcessorProcessingPartitionSize) {
        this.asyncEventSetProcessorProcessingPartitionSize = asyncEventSetProcessorProcessingPartitionSize;
    }

    @Override
    public void setAllowConcurrentEventTypeProcessingByEventSetProcessors(boolean allowConcurrentEventTypeProcessingByEventSetProcessors) {
        this.allowConcurrentEventTypeProcessingByEventSetProcessors = allowConcurrentEventTypeProcessingByEventSetProcessors;
    }

    @Override
    public void setAutoEnable(boolean autoEnable) {
        this.autoEnable = autoEnable;
    }

    @Override
    public int getAsyncEventSetProcessorProcessingThreshold() {
        return asyncEventSetProcessorProcessingThreshold;
    }

    @Override
    public void setAsyncEventSetProcessorProcessingThreshold(int asyncEventSetProcessorProcessingThreshold) {
        this.asyncEventSetProcessorProcessingThreshold = asyncEventSetProcessorProcessingThreshold;
    }

    @Override
    public int getMainProcessingUnitWorkerExecutorQueueSize() {
        return mainProcessingUnitWorkerExecutorQueueSize;
    }

    @Override
    public void setMainProcessingUnitWorkerExecutorQueueSize(int mainProcessingUnitWorkerExecutorQueueSize) {
        this.mainProcessingUnitWorkerExecutorQueueSize = mainProcessingUnitWorkerExecutorQueueSize;
    }

    @Override
    public long getInternalQueueBlockingPutSleep() {
        return internalQueueBlockingPutSleep;
    }

    @Override
    public void setInternalQueueBlockingPutSleep(long internalQueueBlockingPutSleep) {
        this.internalQueueBlockingPutSleep = internalQueueBlockingPutSleep;
    }

    @Override
    public int getDecomposedQueueCapacity() {
        return decomposedQueueCapacity;
    }

    @Override
    public void setDecomposedQueueCapacity(int decomposedQueueCapacity) {
        this.decomposedQueueCapacity = decomposedQueueCapacity;
    }

    @Override
    public int getMainEventQueueCapacity() {
        return mainEventQueueCapacity;
    }

    @Override
    public void setMainEventQueueCapacity(int mainEventQueueCapacity) {
        this.mainEventQueueCapacity = mainEventQueueCapacity;
    }

    @Override
    public double getInitialDynamicThreadPoolSizeRatio() {
        return initialDynamicThreadPoolSizeRatio;
    }

    @Override
    public void setInitialDynamicThreadPoolSizeRatio(double initialDynamicThreadPoolSizeRatio) {
        this.initialDynamicThreadPoolSizeRatio = initialDynamicThreadPoolSizeRatio;
    }

    @Override
    public long getDynamicThreadPoolKeepAliveTime() {
        return dynamicThreadPoolKeepAliveTime;
    }

    @Override
    public void setDynamicThreadPoolKeepAliveTime(long dynamicThreadPoolKeepAliveTime) {
        this.dynamicThreadPoolKeepAliveTime = dynamicThreadPoolKeepAliveTime;
    }
}
