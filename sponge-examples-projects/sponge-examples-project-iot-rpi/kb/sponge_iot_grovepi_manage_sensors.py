"""
Sponge Knowledge Base
GrovePi
"""

class ManageSensorActuatorValues(Action):
    def onConfigure(self):
        self.withLabel("Manage the sensor and actuator values").withDescription("Provides management of the sensor and actuator values.")
        self.withArgs([
            NumberType("temperatureSensor").withNullable().withReadOnly().withLabel(u"Temperature sensor (°C)").withProvided(ProvidedMeta().withValue()),
            NumberType("humiditySensor").withNullable().withReadOnly().withLabel(u"Humidity sensor (%)").withProvided(ProvidedMeta().withValue()),
            NumberType("lightSensor").withNullable().withReadOnly().withLabel(u"Light sensor").withProvided(ProvidedMeta().withValue()),
            NumberType("rotarySensor").withNullable().withReadOnly().withLabel(u"Rotary sensor").withProvided(ProvidedMeta().withValue()),
            NumberType("soundSensor").withNullable().withReadOnly().withLabel(u"Sound sensor").withProvided(ProvidedMeta().withValue()),
            BooleanType("redLed").withLabel("Red LED").withProvided(ProvidedMeta().withValue().withOverwrite()),
            IntegerType("blueLed").withMinValue(0).withMaxValue(255).withLabel("Blue LED").withProvided(ProvidedMeta().withValue().withOverwrite()),
            BooleanType("buzzer").withLabel("Buzzer").withProvided(ProvidedMeta().withValue().withOverwrite())
        ]).withNoResult()
        self.withFeatures({"icon":"thermometer", "refreshEvents":["sensorChange"]})
    def onCall(self, temperatureSensor, humiditySensor, lightSensor, rotarySensor, soundSensor, redLed, blueLed, buzzer):
        grovePiDevice = sponge.getVariable("grovePiDevice")
        grovePiDevice.setRedLed(redLed)
        grovePiDevice.setBlueLed(blueLed)
        grovePiDevice.setBuzzer(buzzer)
    def onProvideArgs(self, context):
        values = sponge.call("GetSensorActuatorValues", [context.provide])
        for name, value in values.iteritems():
            context.provided[name] = ProvidedValue().withValue(value)

class GetSensorActuatorValues(Action):
    def onCall(self, names):
        values = {}
        grovePiDevice = sponge.getVariable("grovePiDevice")
        if "temperatureSensor" or "humiditySensor" in names:
            th = grovePiDevice.getTemperatureHumiditySensor()
            if "temperatureSensor" in names:
                values["temperatureSensor"] = th.temperature if th else None
            if "humiditySensor" in names:
                values["humiditySensor"] = th.humidity if th else None
        if "lightSensor" in names:
            values["lightSensor"] = grovePiDevice.getLightSensor()
        if "rotarySensor" in names:
            values["rotarySensor"] = grovePiDevice.getRotarySensor().factor
        if "soundSensor" in names:
            values["soundSensor"] = grovePiDevice.getSoundSensor()
        if "redLed" in names:
            values["redLed"] = grovePiDevice.getRedLed()
        if "blueLed" in names:
            values["blueLed"] = grovePiDevice.getBlueLed()
        if "buzzer" in names:
            values["buzzer"] = grovePiDevice.getBuzzer()
        return values
