"""
Sponge Knowledge base
Hello World action
"""

class HelloWorldAction(Action):
    def onConfigure(self):
        self.label = "Hello world"
        self.description = "Returns a greeting text."
        self.argsMeta = [ArgMeta("name", StringType()).withLabel("Your name").withDescription("Type your name.")]
        self.resultMeta = ResultMeta(StringType()).withLabel("Greeting").withDescription("The greeting text.")
    def onCall(self, name):
        return "Hello World! Hello {}!".format(name)

def onStartup():
    sponge.logger.info("{}", sponge.call("HelloWorldAction", ["Sponge user"]))
