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

package org.openksavi.sponge.midi;

import org.openksavi.sponge.core.engine.DefaultSpongeEngine;
import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.SpongeEngine;

/**
 * This example program enables a user to play an input MIDI device (e.g. a MIDI keyboard) using the Sponge MIDI plugin.
 */
public class MidiInputMain {

    public void run() {
        SpongeEngine engine = DefaultSpongeEngine.builder().config("examples/midi/midi_input.xml").build();
        engine.startup();
        SpongeUtils.registerShutdownHook(engine);
    }

    /**
     * Main method.
     *
     * @param args arguments.
     */
    public static void main(String... args) {
        new MidiInputMain().run();
    }
}
