/*
 * Sponge Knowledge Base
 * Action builders
 */

package org.openksavi.sponge.kotlin.examples

import org.openksavi.sponge.examples.PowerEchoAction
import org.openksavi.sponge.kotlin.KAction
import org.openksavi.sponge.kotlin.KActionBuilder
import org.openksavi.sponge.kotlin.KKnowledgeBase
import org.openksavi.sponge.type.StringType
import org.openksavi.sponge.type.provided.ProvidedMeta
import org.openksavi.sponge.type.provided.ProvidedValue

class ActionsBuilder : KKnowledgeBase() {

    override fun onInit() {
        // Variables for assertions only
        sponge.setVariable("initialized_UpperEchoAction", false)
        sponge.setVariable("called_UpperEchoActionMulti", false)
        sponge.setVariable("called_NoArgAndResultAction", false)
    }

    override fun onLoad() {
        sponge.enable(KActionBuilder("UpperEchoAction").withLabel("Echo Action").withDescription("Returns the upper case string")
                .withArg(StringType("text").withLabel("Argument 1").withDescription("Argument 1 description"))
                .withResult(StringType().withLabel("Upper case string").withDescription("Result description"))
                .withOnInit({ action -> sponge.setVariable("initialized_" + action.meta.name, true) })
                .withOnCallArgs({ action, args -> (args.get(0) as String).toUpperCase() }))

        sponge.enable(KActionBuilder("UpperEchoActionMulti").withLabel("Echo Action").withDescription("Returns the upper case string")
                .withArg(StringType("text").withLabel("Argument 1").withDescription("Argument 1 description"))
                .withResult(StringType().withLabel("Upper case string").withDescription("Result description"))
                .withOnCallArgs({ action, args -> 
                    sponge.getLogger().info("Action {} called", action.meta.name)
                    sponge.setVariable("called_" + action.meta.name, true)
                    (args.get(0) as String).toUpperCase()
                }))

        sponge.enable(KActionBuilder("NoArgAndResultAction")
                .withOnCallArgsNoResult({action, args -> sponge.setVariable("called_" + action.meta.name, true)}))

        sponge.enable(KActionBuilder("ProvidedArgsAction").withArg(StringType("text").withProvided(ProvidedMeta().withValue()))
                        .withCallable(false).withOnProvideArgs({action, context -> 
                            if (context.getProvide().contains("text")) {
                                context.getProvided().put("text", ProvidedValue<String>().withValue("ABC"))
                            }
                        }))
    }
}

