"""
Sponge Knowledge base
Action category
"""

def onInit():
    sponge.addCategories(CategoryMeta("myActions").label("My actions").description("My actions description"),
                         CategoryMeta("yourActions").label("Your actions").description("Your actions description"),
                         CategoryMeta("notUsedCategory"))

class MyAction1(Action):
    def onConfigure(self):
        self.label = "MyAction 1"
        self.category = "myActions"
    def onCall(self, text):
    	return None

class MyAction2(Action):
    def onConfigure(self):
        self.label = "MyAction 2"
        self.category = "myActions"
    def onCall(self, text):
        return None

class YourAction1(Action):
    def onConfigure(self):
        self.label = "YourAction1 1"
        self.category = "yourActions"
    def onCall(self, text):
        return None

class OtherAction(Action):
    def onCall(self, text):
        return None