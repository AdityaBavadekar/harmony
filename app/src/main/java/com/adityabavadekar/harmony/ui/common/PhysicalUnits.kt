/*
 * Copyright 2024 Aditya Bavadekar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adityabavadekar.harmony.ui.common

enum class MassUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<MassUnits> {
    KG(1.0, "kg"),
    GRAM(1000.0, "g"),
    STONES(6.35029, "St."),
    POUNDS(0.453592, "lb");
}

enum class HeatUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<HeatUnits> {
    JOULES(1.0, "J"),
    CALORIES(4.184, "cal"),
    BTU(1055.06, "BTU");
}

enum class PressureUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<PressureUnits> {
    PASCAL(1.0, "Pa"),
    BAR(100000.0, "bar"),
    ATMOSPHERE(101325.0, "atm"),
    TORR(133.322, "Torr"),
    PSI(6894.76, "psi");
}

enum class SpeedUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<SpeedUnits> {
    METERS_PER_SECOND(1.0, "m/s"),
    KILOMETERS_PER_HOUR(0.277778, "km/hr"),
    MILES_PER_HOUR(0.44704, "miles/hr")
}

enum class LengthUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<LengthUnits> {
    METERS(1.0, "m"),
    KILOMETERS(1000.0, "km"),
    CENTIMETERS(0.01, "cm"),
    MILLIMETERS(0.001, "mm"),
    INCHES(0.0254, "in."),
    FEET(0.3048, "ft"),
    YARDS(0.9144, "yd"),
    MILES(1609.34, "miles")
}

enum class TimeUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<TimeUnits> {
    SECONDS(1.0, "s"),
    MINUTES(60.0, "min"),
    HOURS(3600.0, "hr"),
    DAYS(86400.0, "d")
}

enum class VolumeUnits(override val siConversionFactor: Double, override val symbol: String) :
    SIConvertibleUnits<VolumeUnits> {
    CUBIC_METER(1.0, "m³"),
    LITERS(0.001, "L"),
    MILLILITER(1e-6, "mL"),
    GALLON(0.00378541, "gal"),
    CUBIC_INCH(1.63871e-5, "in³"),
    CUBIC_FOOT(0.0283168, "ft³");
}

enum class TemperatureUnits(override val symbol: String) : SIConvertibleUnits<TimeUnits> {
    CELSIUS("°C"),
    FAHRENHEIT("F"),
    KELVIN("K");

    override val siConversionFactor: Double
        get() = throw IllegalStateException()

    private fun toKelvin(value: Double): Double {
        return when (this) {
            CELSIUS -> value + 273.15
            FAHRENHEIT -> (value - 32) * 5 / 9
            KELVIN -> value
        }
    }

    private fun fromKelvin(value: Double): Double {
        return when (this) {
            CELSIUS -> value - 273.15
            FAHRENHEIT -> value * 9 / 5 + 32
            KELVIN -> value
        }
    }

    override fun toSI(value: Double): Double = toKelvin(value)

    override fun fromSI(value: Double): Double = fromKelvin(value)

}

interface SIConvertibleUnits<T> {
    val siConversionFactor: Double
    val symbol: String
    val ordinal: Int
    val name: String

    fun toSI(value: Double): Double {
        return value * siConversionFactor
    }

    fun fromSI(value: Double): Double {
        return value / siConversionFactor
    }

    fun shortSymbol(): String {
        return symbol
    }

}

class Length(lengthInMeters: Double) : PhysicalUnit<LengthUnits>() {
    constructor(lengthInMeters: Float) : this(lengthInMeters.toDouble())

    var unit: LengthUnits = LengthUnits.METERS
        private set
    private var length: Double = lengthInMeters
    override fun getValue(targetUnit: LengthUnits): Double {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return length * unit.siConversionFactor
    }
}

class Mass(massInKg: Double) : PhysicalUnit<MassUnits>() {

    constructor(massInKg: Float) : this(massInKg.toDouble())

    var unit: MassUnits = MassUnits.KG
        private set
    private var mass: Double = massInKg
    override fun getValue(targetUnit: MassUnits): Double {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return mass * unit.siConversionFactor
    }
}

class Temperature(temperatureInCelsius: Double) : PhysicalUnit<TemperatureUnits>() {

    constructor(temperatureInCelsius: Float) : this(temperatureInCelsius.toDouble())

    var unit: TemperatureUnits = TemperatureUnits.CELSIUS
        private set
    private var temperature: Double = temperatureInCelsius
    override fun getValue(targetUnit: TemperatureUnits): Double {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return unit.toSI(temperature)
    }
}

class Speed constructor(speedInMetersSec: Double) : PhysicalUnit<SpeedUnits>() {

    constructor(speedInMetersSec: Float) : this(speedInMetersSec.toDouble())

    var unit: SpeedUnits = SpeedUnits.METERS_PER_SECOND
        private set
    private var speed: Double = speedInMetersSec
    override fun getValue(targetUnit: SpeedUnits): Double {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return speed * unit.siConversionFactor
    }
}

class Volume(volumeInLitres: Float) : PhysicalUnit<SpeedUnits>() {
    var unit: VolumeUnits = VolumeUnits.LITERS
        private set
    private var volume = volumeInLitres
    override fun getValue(targetUnit: SpeedUnits): Double {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return volume * unit.siConversionFactor
    }

}

class Pressure(pressureInPascals: Float) : PhysicalUnit<PressureUnits>() {
    var unit: PressureUnits = PressureUnits.PASCAL
        private set
    private var pressure = pressureInPascals

    override fun getValue(targetUnit: PressureUnits): Double {
        return targetUnit.fromSI(getSIValue())
    }

    override fun getSIValue(): Double {
        return pressure * unit.siConversionFactor
    }
}

abstract class PhysicalUnit<T> {
    abstract fun getValue(targetUnit: T): Double
    abstract fun getSIValue(): Double

}