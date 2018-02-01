/*
 * Sponge Knowledge base
 * Hello world
 */

package org.openksavi.sponge.kotlin.examples

import org.openksavi.sponge.event.Event
import org.openksavi.sponge.kotlin.KKnowledgeBase
import org.openksavi.sponge.kotlin.KTrigger

class HelloWorld : KKnowledgeBase() {

    class HelloWorld : KTrigger() {
        override fun onConfigure() {
            setEvent("helloEvent")
        }

        override fun onRun(event: Event) {
            println(event.get<String>("say"))
        }
    }

    override fun onStartup() {
        eps.event("helloEvent").set("say", "Hello World!").send()
    }
}