"""
Sponge Knowledge Base
Correlators - Event pattern
"""

from java.util.concurrent.atomic import AtomicInteger

def onInit():
    # Variables for assertions only
    sponge.setVariable("nameCount", AtomicInteger(0))
    sponge.setVariable("patternCount", AtomicInteger(0))

class NameCorrelator(Correlator):
    def onConfigure(self):
        self.withEvents(["a1", "b1"]).withMaxInstances(1)
    def onEvent(self, event):
        sponge.getVariable("nameCount").incrementAndGet()

class PatternCorrelator(Correlator):
    def onConfigure(self):
        self.withEvents(["a.+", "b.*"]).withMaxInstances(1)
    def onEvent(self, event):
        sponge.getVariable("patternCount").incrementAndGet()

def onStartup():
    for name in ["a1", "b1", "a2", "b2", "a", "b", "a1", "b2"]:
        sponge.event(name).send()
