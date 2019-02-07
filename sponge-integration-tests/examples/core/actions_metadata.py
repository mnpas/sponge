"""
Sponge Knowledge base
Action argument definitions
"""

from org.openksavi.sponge.examples import PowerEchoMetadataAction

def onInit():
    # Variables for assertions only
    sponge.setVariable("scriptActionResult", None)
    sponge.setVariable("javaActionResult", None)

class UpperEchoAction(Action):
    def onConfigure(self):
        self.label = "Echo Action"
        self.description = "Returns the upper case string"
        self.argsMeta = [ ArgMeta("text", StringType()).withLabel("Argument 1").withDescription("Argument 1 description") ]
        self.resultMeta = ResultMeta(StringType()).withLabel("Upper case string").withDescription("Result description")
    def onCall(self, text):
        self.logger.info("Action {} called", self.name)
    	return text.upper()

def onLoad():
    sponge.enableJava(PowerEchoMetadataAction)

def onStartup():
    sponge.logger.debug("Calling script defined action")
    scriptActionResult = sponge.call("UpperEchoAction", ["test"])
    sponge.logger.debug("Action returned: {}", scriptActionResult)
    sponge.setVariable("scriptActionResult", scriptActionResult)

    sponge.logger.debug("Calling Java defined action")
    javaActionResult = sponge.call("PowerEchoMetadataAction", [1, "test"])
    sponge.logger.debug("Action returned: {}", javaActionResult)
    sponge.setVariable("javaActionResult", javaActionResult)

