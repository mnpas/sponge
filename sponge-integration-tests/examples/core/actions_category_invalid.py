"""
Sponge Knowledge base
Action invalid category
"""

def onInit():
    sponge.addCategory(CategoryMeta("myActions").label("My actions").description("My actions description"))

class MyAction1(Action):
    def onConfigure(self):
        self.label = "MyAction 1"
        self.category = "yourActions"
    def onCall(self, text):
    	return None