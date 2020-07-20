"""
Sponge Knowledge Base
Using filters for deduplication of events.
"""

from java.util import Collections, HashMap
from java.util.concurrent.atomic import AtomicInteger
from java.util.concurrent import TimeUnit

from org.openksavi.sponge.core.library import Deduplication

def onInit():
    global eventCounter
    # Variables for assertions only
    eventCounter = Collections.synchronizedMap(HashMap())
    eventCounter.put("e1-blue", AtomicInteger(0))
    eventCounter.put("e1-red", AtomicInteger(0))
    eventCounter.put("e2-blue", AtomicInteger(0))
    eventCounter.put("e2-red", AtomicInteger(0))
    sponge.setVariable("eventCounter", eventCounter)

class ColorDeduplicationFilter(Filter):
    def onConfigure(self):
        self.withEvent("e1")
    def onInit(self):
        self.deduplication = Deduplication("color")
        self.deduplication.cacheBuilder.maximumSize(1000).expireAfterWrite(5, TimeUnit.MINUTES)
    def onAccept(self, event):
        return self.deduplication.onAccept(event)

class ColorTrigger(Trigger):
    def onConfigure(self):
        self.withEvents(["e1", "e2"])
    def onRun(self, event):
        self.logger.debug("Received event {}", event)
        global eventCounter
        eventCounter.get(event.name + "-" + event.get("color")).incrementAndGet()

def onStartup():
    sponge.event("e1").set("color", "red").send()
    sponge.event("e1").set("color", "blue").send()
    sponge.event("e2").set("color", "red").send()
    sponge.event("e2").set("color", "blue").send()

    sponge.event("e1").set("color", "red").send()
    sponge.event("e1").set("color", "blue").send()
    sponge.event("e2").set("color", "red").send()
    sponge.event("e2").set("color", "blue").send()
