"""
Sponge Knowledge base
"""

# Plugin written in Python. Stores any value.
class StoragePlugin(Plugin):
    def onConfigure(self, configuration):
        self.storedValue = configuration.getString("storedValue", "default")
    def onInit(self):
        self.logger.debug("Initializing {}", self.name)
    def onStartup(self):
        self.logger.debug("Starting up {}", self.name)
    def getStoredValue(self):
        return self.storedValue
    def setStoredValue(self, value):
        self.storedValue = value