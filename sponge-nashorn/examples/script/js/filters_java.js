/**
 * Sponge Knowledge Base
 * Using java filters 
 */

var AtomicInteger = java.util.concurrent.atomic.AtomicInteger;

function onInit() {
    // Variables for assertions only
    var eventCounter = java.util.Collections.synchronizedMap(new java.util.HashMap());
    eventCounter.put("e1", new AtomicInteger(0));
    eventCounter.put("e2", new AtomicInteger(0));
    eventCounter.put("e3", new AtomicInteger(0));
    sponge.setVariable("eventCounter", eventCounter);
}

var FilterTrigger = Java.extend(Trigger, {
    onConfigure: function(self) {
        self.withEvents(["e1", "e2", "e3"]);
    },
    onRun: function(self, event) {
        self.logger.debug("Processing trigger for event {}", event);
        sponge.getVariable("eventCounter").get(event.name).incrementAndGet();
    }
});

function onLoad() {
    sponge.enableJava(org.openksavi.sponge.examples.ShapeFilter.class);
}

function onStartup() {
    sponge.event("e1").sendAfter(100, 100);
    sponge.event("e2").set("shape", "square").sendAfter(200, 100);
    sponge.event("e3").set("shape", "circle").sendAfter(300, 100);
}
