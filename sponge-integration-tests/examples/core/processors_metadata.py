"""
Sponge Knowledge Base
Processors metadata
"""

from org.openksavi.sponge.examples import PowerEchoMetadataAction

class UpperEchoAction(Action):
    def onConfigure(self):
        self.withFeatures({"visibility":False})
    def onCall(self, text):
    	return None

def onLoad():
    sponge.enableJava(PowerEchoMetadataAction)
