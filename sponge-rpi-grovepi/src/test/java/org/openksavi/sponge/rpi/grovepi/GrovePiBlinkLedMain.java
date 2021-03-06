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

package org.openksavi.sponge.rpi.grovepi;

import org.openksavi.sponge.core.engine.DefaultSpongeEngine;
import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.logging.LoggingUtils;

/**
 * This example program shows how to turn on/off a LED connected to the GrovePi board that in turn is connected to the Raspberry Pi.
 */
public class GrovePiBlinkLedMain {

    public void run() {
        LoggingUtils.initLoggingBridge();

        SpongeEngine engine = DefaultSpongeEngine.builder().config("examples/rpi-grovepi/grovepi_led_blink.xml").build();
        engine.startup();
        SpongeUtils.registerShutdownHook(engine);
    }

    /**
     * Main method.
     *
     * @param args arguments.
     */
    public static void main(String... args) {
        new GrovePiBlinkLedMain().run();
    }
}
