"""
Sponge Knowledge Base
Triggers - Incorrect event pattern
"""

class TriggerAPattern(Trigger):
    def onConfigure(self):
        self.withEvent("a.**")
    def onRun(self, event):
        pass
