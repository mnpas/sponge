"""
Sponge Knowledge base
Hello world
"""

class HelloWorldTrigger(Trigger):
    def onConfigure(self):
        self.event = "helloEvent"
    def onRun(self, event):
        print event.get("say")

def onStartup():
    sponge.event("helloEvent").set("say", "Hello World!").send()