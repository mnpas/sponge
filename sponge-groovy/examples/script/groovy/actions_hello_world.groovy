/**
 * Sponge Knowledge Base
 * Hello World action
 */

class HelloWorldAction extends Action {
    void onConfigure() {
        this.withLabel("Hello world").withDescription("Returns a greeting text.")
        this.withArg(new StringType("name").withLabel("Your name").withDescription("Type your name."))
        this.withResult(new StringType().withLabel("Greeting").withDescription("The greeting text."))
    }

    String onCall(String name) {
        return "Hello World! Hello $name!"
    }
}

void onStartup() {
    sponge.logger.info("{}", sponge.call("HelloWorldAction", ["Sponge user"]))
}
