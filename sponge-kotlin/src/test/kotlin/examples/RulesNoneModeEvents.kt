/*
 * Sponge Knowledge Base
 * Using rules - events
 */

package org.openksavi.sponge.kotlin.examples

import org.openksavi.sponge.event.Event
import org.openksavi.sponge.kotlin.KKnowledgeBase
import org.openksavi.sponge.kotlin.KRule
import org.openksavi.sponge.examples.util.CorrelationEventsLog
import java.time.Duration

class RulesNoneModeEvents : KKnowledgeBase() {

    companion object {
        val correlationEventsLog = CorrelationEventsLog()
    }

    override fun onInit() {
        // Variables for assertions only
        sponge.setVariable("correlationEventsLog", correlationEventsLog)
    }

    // Naming F(irst), L(ast), A(ll), N(one)

    class RuleFNNF : KRule() {
        override fun onConfigure() {
            withEvents("e1", "e5 :none", "e6 :none", "e3")
        }

        override fun onRun(event: Event?) {
            logger.debug("Running rule for events: {}", eventAliasMap)
            correlationEventsLog.addEvents(meta.name, this)
        }
    }

    class RuleFNNNL : KRule() {
        override fun onConfigure() {
            withEvents("e1", "e5 :none", "e6 :none", "e7 :none", "e3 :last").withDuration(Duration.ofSeconds(2))
        }

        override fun onRun(event: Event?) {
            logger.debug("Running rule for events: {}", eventAliasMap)
            correlationEventsLog.addEvents(meta.name, this)
        }
    }

    class RuleFNNNLReject : KRule() {
        override fun onConfigure() {
            withEvents("e1", "e5 :none", "e2 :none", "e7 :none", "e3 :last").withDuration(Duration.ofSeconds(2))
        }

        override fun onRun(event: Event?) {
            logger.debug("Running rule for events: {}", eventAliasMap)
            correlationEventsLog.addEvents(meta.name, this)
        }
    }

    class RuleFNFNL : KRule() {
        override fun onConfigure() {
            withEvents("e1", "e5 :none", "e2", "e7 :none", "e3 :last").withDuration(Duration.ofSeconds(2))
        }

        override fun onRun(event: Event?) {
            logger.debug("Running rule for events: {}", eventAliasMap)
            correlationEventsLog.addEvents(meta.name, this)
        }
    }

    override fun onStartup() {
        sponge.event("e1").set("label", "1").send()
        sponge.event("e2").set("label", "2").send()
        sponge.event("e2").set("label", "3").send()
        sponge.event("e2").set("label", "4").send()
        sponge.event("e3").set("label", "5").send()
        sponge.event("e3").set("label", "6").send()
        sponge.event("e3").set("label", "7").send()
    }
}
