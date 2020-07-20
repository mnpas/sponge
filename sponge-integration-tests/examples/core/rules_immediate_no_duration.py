"""
Sponge Knowledge Base
Using rules - immediate, no duration
Note that auto-enable is turned off in the configuration.
"""

from org.openksavi.sponge.examples.util import CorrelationEventsLog

def onInit():
    global correlationEventsLog

    # Variables for assertions only
    correlationEventsLog = CorrelationEventsLog()
    sponge.setVariable("correlationEventsLog", correlationEventsLog)

def runRule(rule):
    rule.logger.debug("Sequence: {}", SpongeUtils.getAbbreviatedEventSequenceString(rule))
    global correlationEventsLog
    correlationEventsLog.addEvents(rule.meta.name, rule)

# Naming F(irst), L(ast), A(ll), N(one)

# F(irst)F(irst)F(irst)
class RuleFFF(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2", "e3 :first"])
    def onRun(self, event):
        runRule(self)

# F(irst)F(irst)L(ast)
class RuleFFL(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2", "e3 :last"])
    def onRun(self, event):
        runRule(self)

# F(irst)F(irst)A(ll)
class RuleFFA(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2", "e3 :all"])
    def onRun(self, event):
        runRule(self)

# F(irst)F(irst)N(one)
class RuleFFN(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2", "e4 :none"])
    def onRun(self, event):
        runRule(self)

# F(irst)L(ast)F(irst)
class RuleFLF(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :last", "e3 :first"])
    def onRun(self, event):
        runRule(self)

# F(irst)L(ast)L(ast)
class RuleFLL(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :last", "e3 :last"])
    def onRun(self, event):
        runRule(self)

# F(irst)L(ast)A(ll)
class RuleFLA(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :last", "e3 :all"])
    def onRun(self, event):
        runRule(self)

# F(irst)L(ast)N(one)
class RuleFLN(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :last", "e4 :none"])
    def onRun(self, event):
        runRule(self)

# F(irst)A(ll)F(irst)
class RuleFAF(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :all", "e3 :first"])
    def onRun(self, event):
        runRule(self)

# F(irst)A(ll)L(ast)
class RuleFAL(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :all", "e3 :last"])
    def onRun(self, event):
        runRule(self)

# F(irst)A(ll)A(ll)
class RuleFAA(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :all", "e3 :all"])
    def onRun(self, event):
        runRule(self)

# F(irst)A(ll)N(one)
class RuleFAN(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :all", "e5 :none"])
    def onRun(self, event):
        runRule(self)

# F(irst)N(one)F(irst)
class RuleFNF(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e5 :none", "e3"])
    def onRun(self, event):
        runRule(self)

# F(irst)N(one)L(ast)
class RuleFNL(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e5 :none", "e3 :last"])
    def onRun(self, event):
        runRule(self)

# F(irst)N(one)A(ll)
class RuleFNA(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e5 :none", "e3 :all"])
    def onRun(self, event):
        runRule(self)

class RuleFNFReject(Rule):
    def onConfigure(self):
        self.withEvents(["e1", "e2 :none", "e3"])
    def onRun(self, event):
        runRule(self)

def onLoad():
    sponge.enableAll(
                RuleFFF,
                RuleFFA,
                RuleFNF, RuleFNFReject,
                RuleFNA,
                RuleFLF,
                RuleFLA,
                RuleFAF,
                RuleFAA
                )

def onStartup():
    sponge.event("e1").set("label", "1").send()
    sponge.event("e2").set("label", "2").send()
    sponge.event("e2").set("label", "3").send()
    sponge.event("e3").set("label", "4").send()
    sponge.event("e2").set("label", "5").send()
    sponge.event("e3").set("label", "6").send()
    sponge.event("e3").set("label", "7").send()
