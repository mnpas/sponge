/**
 * Sponge Knowledge base
 * Error reporting
 */

class HelloWorldTrigger extends Trigger {
    void onConfigure() {
        this.withEvent("helloEvent")
    }
    void onRun(Event event) {
        whatIsThis.doSomething()
        println event.get("say")
    }
}

void onStartup() {
    sponge.event("helloEvent").set("say", "Hello World!").send()
}
