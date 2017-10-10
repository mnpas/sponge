"""
Sponge Knowledge base
"""

from java.util.concurrent.atomic import AtomicBoolean
import re, collections

# Reject news with empty or short titles.
class NewsFilter(Filter):
    def onConfigure(self):
        self.event = "news"
    def onAccept(self, event):
        title = event.get("title")
        words = len(re.findall("\w+", title))
        return title is not None and words >= int(EPS.getVariable("newsFilterWordThreshold"))

# Log every news.
class LogNewsTrigger(Trigger):
    def onConfigure(self):
        self.event = "news"
    def onRun(self, event):
        print("News from {} - {}".format(event.get("source"), event.get("title")))

# Send 'alarm' event when news stop arriving for 3 seconds.
class NoNewNewsAlarmRule(Rule):
    def onConfigure(self):
        self.events = ["news n1", "news n2 :none"]
        self.duration = Duration.ofSeconds(3)
    def onRun(self, event):
        EPS.event("alarm").set("severity", 1).set("message", "No new news for " + str(self.duration.seconds) + "s.").send()

# Handles 'alarm' events.
class AlarmTrigger(Trigger):
    def onConfigure(self):
        self.event = "alarm"
    def onRun(self, event):
        print("Sound the alarm! {}".format(event.get("message")))
        print("Last news was:\n{}".format(EPS.call("EmphasizeAction", storagePlugin.storedValue[-1])))
        EPS.getVariable("alarmSounded").set(True)

# Start only one instance of this correlator for the system. Note that in this example data is stored in a plugin,
# not in this correlator.
class LatestNewsCorrelator(Correlator):
    instanceStarted = AtomicBoolean(False)
    def onConfigure(self):
        self.events = ["news"]
    def onAcceptAsFirst(self, event):
        return LatestNewsCorrelator.instanceStarted.compareAndSet(False, True)
    def onInit(self):
        storagePlugin.storedValue = collections.deque(maxlen=int(EPS.getVariable("latestNewsMaxSize", 2)))
    def onEvent(self, event):
        storagePlugin.storedValue.append(event.get("title"))
        self.logger.debug("{} - latest news: {}", self.hashCode(), str(storagePlugin.storedValue))

