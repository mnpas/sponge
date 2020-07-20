/*
 * Sponge Knowledge Base
 * Using rules
 */

package org.openksavi.sponge.kotlin.examples

import org.openksavi.sponge.event.Event
import org.openksavi.sponge.examples.SameSourceJavaRule
import org.openksavi.sponge.kotlin.KKnowledgeBase
import org.openksavi.sponge.kotlin.KRule
import org.openksavi.sponge.rule.EventCondition
import org.openksavi.sponge.rule.Rule
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class Rules : KKnowledgeBase() {

    override fun onInit() {
        // Variables for assertions only
        sponge.setVariable("hardwareFailureJavaCount", AtomicInteger(0))
        sponge.setVariable("hardwareFailureScriptCount", AtomicInteger(0))
        sponge.setVariable("sameSourceFirstFireCount", AtomicInteger(0))
    }

    class FirstRule : KRule() {
        override fun onConfigure() {
            // Events specified without aliases
            withEvents("filesystemFailure", "diskFailure").withCondition("diskFailure", { rule: Rule, event: Event ->
                Duration.between(rule.getEvent("filesystemFailure").time, event.time).seconds >= 0
            })
        }

        override fun onRun(event: Event?) {
            logger.debug("Running rule for event: {}", event?.name)
            sponge.getVariable<AtomicInteger>("sameSourceFirstFireCount").incrementAndGet()
        }
    }

    class SameSourceAllRule : KRule() {
        override fun onConfigure() {
            // Events specified with aliases (e1 and e2)
            withEvents("filesystemFailure e1", "diskFailure e2 :all")
            withCondition("e1", this::severityCondition).withConditions("e2", this::severityCondition, this::diskFailureSourceCondition)
            withDuration(Duration.ofSeconds(8))
        }

        override fun onRun(event: Event?) {
            logger.info("Monitoring log [{}]: Critical failure in {}! Events: {}", event?.time, event?.get("source"),
                    eventSequence)
            sponge.getVariable<AtomicInteger>("hardwareFailureScriptCount").incrementAndGet()
        }

        fun severityCondition(event: Event) = event.get<Int>("severity") > 5

        fun diskFailureSourceCondition(event: Event): Boolean {
            // Both events have to have the same source
            val event1 = this.getEvent("e1")
            return event.get<Any>("source") == event1.get<Any>("source") && Duration.between(event1.time, event.time).seconds <= 4
        }
    }

    override fun onLoad() = sponge.enableJava(SameSourceJavaRule::class.java)

    override fun onStartup() {
        sponge.event("filesystemFailure").set("severity", 8).set("source", "server1").send()
        sponge.event("diskFailure").set("severity", 10).set("source", "server1").send()
        sponge.event("diskFailure").set("severity", 10).set("source", "server2").send()
        sponge.event("diskFailure").set("severity", 8).set("source", "server1").send()
        sponge.event("diskFailure").set("severity", 8).set("source", "server1").send()
        sponge.event("diskFailure").set("severity", 1).set("source", "server1").send()
    }
}

@Suppress("UNUSED_PARAMETER")
fun alwaysTrueRuleEventCondition(rule: Rule, event: Event) = true
