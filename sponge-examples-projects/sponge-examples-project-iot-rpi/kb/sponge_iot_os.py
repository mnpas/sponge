"""
Sponge Knowledge base
OS commands
"""

from org.openksavi.sponge.util.process import ProcessConfiguration

class OsGetDiskSpaceInfo(Action):
    def onConfigure(self):
        self.withLabel("Get disk space info").withDescription("Returns the disk space info.")
        self.withNoArgs().withResult(ResultMeta(StringType().withFormat("console")).withLabel("Disk space info"))
    def onCall(self):
        return sponge.process(ProcessConfiguration.builder("df", "-h").outputAsString()).run().outputString

class OsDmesg(Action):
    def onConfigure(self):
        self.withLabel("Run dmesg").withDescription("Returns the dmesg output.")
        self.withNoArgs().withResult(ResultMeta(StringType().withFormat("console")).withLabel("The dmesg output"))
    def onCall(self):
        return sponge.process(ProcessConfiguration.builder("dmesg").outputAsString()).run().outputString
