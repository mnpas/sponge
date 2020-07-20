"""
Sponge Knowledge Base
Action onConfigure error
"""

class TestAction(Action):
    def onConfigure(self):
        self.withNoArgs().withResult(StringType().label_error("Test action"))
    def onCall(self):
        return None
