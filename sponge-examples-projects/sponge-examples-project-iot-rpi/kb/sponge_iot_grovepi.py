"""
Sponge Knowledge base
GrovePi
"""

def onStartup():
    sponge.setVariable("grovePiDevice", GrovePiDevice())
    # Grove Pi mode: auto, manual
    sponge.setVariable("grovePiMode", "auto")

class SetGrovePiMode(Action):
    def onConfigure(self):
        self.withLabel("Set the GrovePi mode").withDescription("Sets the GrovePi mode.")
        self.withArg(StringType("mode").withLabel("The GrovePi mode").withProvided(ProvidedMeta().withValue().withValueSet().withOverwrite()))
        self.withNoResult()
        self.withFeature("icon", "raspberry-pi")
    def onCall(self, mode):
        if mode not in ["auto", "manual"]:
            raise Exception("Unsupported GrovePi mode: " + mode)
        sponge.setVariable("grovePiMode", mode)
    def onProvideArgs(self, context):
        if "mode" in context.provide:
            context.provided["mode"] = ProvidedValue().withValue(sponge.getVariable("grovePiMode", None)).withAnnotatedValueSet([
                AnnotatedValue("auto").withValueLabel("Auto"), AnnotatedValue("manual").withValueLabel("Manual")])

class GetLcdText(Action):
    def onConfigure(self):
        self.withLabel("Get the LCD text").withDescription("Returns the LCD text.")
        self.withNoArgs().withResult(StringType().withFeatures({"maxLines":5}).withLabel("LCD Text"))
        self.withFeature("icon", "monitor-dashboard")
    def onCall(self):
        return sponge.getVariable("grovePiDevice").getLcdText()
