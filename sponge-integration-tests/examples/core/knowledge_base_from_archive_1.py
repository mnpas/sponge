"""
Sponge Knowledge Base
Test - KB from an archive file
"""

class Action1FromArchive(Action):
    def onCall(self, arg):
        return arg.upper()

