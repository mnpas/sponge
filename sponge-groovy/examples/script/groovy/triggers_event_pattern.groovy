/**
 * Sponge Knowledge Base
 * Triggers - Event pattern
 */

import java.util.concurrent.atomic.AtomicInteger

void onInit() {
    // Variables for assertions only
    sponge.setVariable("countA", new AtomicInteger(0))
    sponge.setVariable("countAPattern", new AtomicInteger(0))
}

class TriggerA extends Trigger {
    void onConfigure() {
        this.withEvent("a")
    }
    void onRun(Event event) {
        sponge.getVariable("countA").incrementAndGet()
    }
}

class TriggerAPattern extends Trigger {
    void onConfigure() {
        this.withEvent("a.*")
    }
    void onRun(Event event) {
        this.logger.debug("Received matching event {}", event.name)
        sponge.getVariable("countAPattern").incrementAndGet()
    }
}

void onStartup() {
    for (name in ["a", "a1", "a2", "aTest", "b1", "b2", "bTest", "a", "A", "A1" ]) {
        sponge.event(name).send()
    }
}
