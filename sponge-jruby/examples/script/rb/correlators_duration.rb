# Sponge Knowledge Base
# Using correlator duration

java_import java.util.concurrent.atomic.AtomicInteger
java_import java.util.concurrent.atomic.AtomicBoolean

def onInit
    # Variables for assertions only
    $sponge.setVariable("hardwareFailureScriptCount", AtomicInteger.new(0))
end

class SampleCorrelator < Correlator
    @@instanceStarted = AtomicBoolean.new(false)
    def onConfigure
        self.withEvents(["filesystemFailure", "diskFailure"]).withDuration(Duration.ofSeconds(2))
    end
    def onAcceptAsFirst(event)
        return @@instanceStarted.compareAndSet(false, true)
    end
    def onInit
        @eventLog = []
    end
    def onEvent(event)
        @eventLog  << event
        $sponge.getVariable("hardwareFailureScriptCount").incrementAndGet()
    end
    def onDuration()
        self.logger.debug("{} - log: {}", self.hashCode(), @eventLog.map(&:to_s))
    end
end

def onStartup
    $sponge.event("filesystemFailure").set("source", "server1").send()
    $sponge.event("diskFailure").set("source", "server1").sendAfter(200, 100)
    $sponge.event("diskFailure").set("source", "server2").sendAfter(200, 100)
end
