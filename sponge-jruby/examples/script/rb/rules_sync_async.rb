# Sponge Knowledge Base
# Using rules - synchronous and asynchronous

java_import org.openksavi.sponge.examples.util.CorrelationEventsLog

def onInit
    # Variables for assertions only
    $correlationEventsLog = CorrelationEventsLog.new
    $sponge.setVariable("correlationEventsLog", $correlationEventsLog)
end

class RuleFFF < Rule
    def onConfigure
        self.withEvents(["e1", "e2", "e3 :first"]).withSynchronous(true)
    end
    def onRun(event)
        self.logger.debug("Running rule for event: {}", event.name)
        $correlationEventsLog.addEvents("RuleFFF", self)
    end
end

class RuleFFL < Rule
    def onConfigure
        self.withEvents(["e1", "e2", "e3 :last"]).withDuration(Duration.ofSeconds(2)).withSynchronous(false)
    end
    def onRun(event)
        self.logger.debug("Running rule for event: {}", event.name)
        $correlationEventsLog.addEvents("RuleFFL", self)
    end
end

def onStartup
    $sponge.event("e1").set("label", "1").send()
    $sponge.event("e2").set("label", "2").send()
    $sponge.event("e2").set("label", "3").send()
    $sponge.event("e2").set("label", "4").send()
    $sponge.event("e3").set("label", "5").send()
    $sponge.event("e3").set("label", "6").send()
    $sponge.event("e3").set("label", "7").send()
end
