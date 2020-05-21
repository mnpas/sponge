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

package org.openksavi.sponge.core.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.openksavi.sponge.engine.WrappedException;

public class WrappedExceptionTest {

    @Test
    public void testWrappedException() {
        assertEquals("java.lang.IllegalArgumentException: Illegal arg at source",
                new WrappedException("source", new Exception(new IllegalArgumentException("Illegal arg"))).getMessage());
        assertEquals("Illegal arg at source",
                new WrappedException("source", new IllegalArgumentException("Illegal arg", new Exception("cause"))).getMessage());
        assertEquals("Illegal arg at source", new WrappedException("source", new IllegalArgumentException("Illegal arg")).getMessage());
        assertEquals("Important message at source",
                new WrappedException("source", "Important message", new IllegalArgumentException("Illegal arg")).getMessage());
    }
}
