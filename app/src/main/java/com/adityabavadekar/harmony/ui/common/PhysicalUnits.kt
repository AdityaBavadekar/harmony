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

enum class MassUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<MassUnits> {
    KG(1f, "kg"),
    GRAM(1000f, "g"),
    STONES(6.35029f, "St."),
    POUNDS(0.453592f, "lb");
}

enum class PressureUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<PressureUnits> {
    PASCAL(1f, "Pa"),
    BAR(100000f, "bar"),
    ATMOSPHERE(101325f, "atm"),
    TORR(133.322f, "Torr"),
    PSI(6894.76f, "psi");
}


val Int.kg: Float get() = this * MassUnits.KG.siConversionFactor
val Int.stones: Float get() = this * MassUnits.STONES.siConversionFactor
val Int.pounds: Float get() = this * MassUnits.POUNDS.siConversionFactor

enum class SpeedUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<SpeedUnits> {
    METERS_PER_SECOND(1f, "m/s"),
    KILOMETERS_PER_HOUR(0.277778f, "km/hr"),
    MILES_PER_HOUR(0.44704f, "miles/hr")
}

enum class LengthUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<LengthUnits> {
    METERS(1f, "m"),
    KILOMETERS(1000f, "km"),
    CENTIMETERS(0.01f, "cm"),
    MILLIMETERS(0.001f, "mm"),
    INCHES(0.0254f, "in."),
    FEET(0.3048f, "ft"),
    YARDS(0.9144f, "yd"),
    MILES(1609.34f, "miles")
}


val Int.meters: Float get() = this * LengthUnits.METERS.siConversionFactor
val Int.kilometers: Float get() = this * LengthUnits.KILOMETERS.siConversionFactor
val Int.centimeters: Float get() = this * LengthUnits.CENTIMETERS.siConversionFactor
val Int.millimeters: Float get() = this * LengthUnits.MILLIMETERS.siConversionFactor
val Int.inches: Float get() = this * LengthUnits.INCHES.siConversionFactor
val Int.feet: Float get() = this * LengthUnits.FEET.siConversionFactor
val Int.yards: Float get() = this * LengthUnits.YARDS.siConversionFactor
val Int.miles: Float get() = this * LengthUnits.MILES.siConversionFactor

enum class TimeUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<TimeUnits> {
    SECONDS(1f, "SECONDS"),
    MINUTES(60f, "MINUTES"),
    HOURS(3600f, "HOURS"),
    DAYS(86400f, "DAYS")
}

val Int.sec: Float get() = this * TimeUnits.SECONDS.siConversionFactor
val Int.min: Float get() = this * TimeUnits.MINUTES.siConversionFactor
val Int.hrs: Float get() = this * TimeUnits.HOURS.siConversionFactor
val Int.days: Float get() = this * TimeUnits.DAYS.siConversionFactor

enum class VolumeUnits(override val siConversionFactor: Float, override val symbol: String) :
    SIConvertibleUnits<VolumeUnits> {
    CUBIC_METER(1f, "m³"),
    LITERS(0.001f, "L"),
    MILLILITER(1e-6f, "mL"),
    GALLON(0.00378541f, "gal"),
    CUBIC_INCH(1.63871e-5f, "in³"),
    CUBIC_FOOT(0.0283168f, "ft³");
}

enum class TemperatureUnits(override val symbol: String) : SIConvertibleUnits<TimeUnits> {
    CELSIUS("°C"),
    FAHRENHEIT("F"),
    KELVIN("K");

    override val siConversionFactor: Float
        get() = throw IllegalStateException()

    private fun toKelvin(value: Float): Float {
        return when (this) {
            CELSIUS -> value + 273.15f
            FAHRENHEIT -> (value - 32) * 5 / 9
            KELVIN -> value
        }
    }

    private fun fromKelvin(value: Float): Float {
        return when (this) {
            CELSIUS -> value - 273.15f
            FAHRENHEIT -> value * 9 / 5 + 32
            KELVIN -> value
        }
    }

    override fun toSI(value: Float): Float = toKelvin(value)

    override fun fromSI(value: Float): Float = fromKelvin(value)

}

interface SIConvertibleUnits<T> {
    val siConversionFactor: Float
    val symbol: String
    val ordinal: Int
    val name: String

    fun toSI(value: Float): Float {
        return value * siConversionFactor
    }

    fun fromSI(value: Float): Float {
        return value / siConversionFactor
    }

    fun shortSymbol(): String {
        return symbol
    }

}

class Length(lengthInMeters: Float) : PhysicalUnit<LengthUnits>() {
    var unit: LengthUnits = LengthUnits.METERS
        private set
    private var length = lengthInMeters
    override fun getValue(targetUnit: LengthUnits): Float {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return length * unit.siConversionFactor
    }
}

class Mass(massInKg: Float) : PhysicalUnit<MassUnits>() {
    var unit: MassUnits = MassUnits.KG
        private set
    private var mass = massInKg
    override fun getValue(targetUnit: MassUnits): Float {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return mass * unit.siConversionFactor
    }
}

class Temperature(temperatureInCelsius: Float) : PhysicalUnit<TemperatureUnits>() {
    var unit: TemperatureUnits = TemperatureUnits.CELSIUS
        private set
    private var temperature = temperatureInCelsius
    override fun getValue(targetUnit: TemperatureUnits): Float {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return unit.toSI(temperature)
    }
}

class Speed(speedInMetersSec: Float) : PhysicalUnit<SpeedUnits>() {
    var unit: SpeedUnits = SpeedUnits.METERS_PER_SECOND
        private set
    private var speed = speedInMetersSec
    override fun getValue(targetUnit: SpeedUnits): Float {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return speed * unit.siConversionFactor
    }
}

class Volume(volumeInLitres: Float) : PhysicalUnit<SpeedUnits>() {
    var unit: VolumeUnits = VolumeUnits.LITERS
        private set
    private var volume = volumeInLitres
    override fun getValue(targetUnit: SpeedUnits): Float {
        return unit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return volume * unit.siConversionFactor
    }

}

class Pressure(pressureInPascals: Float) : PhysicalUnit<PressureUnits>() {
    var unit: PressureUnits = PressureUnits.PASCAL
        private set
    private var pressure = pressureInPascals

    override fun getValue(targetUnit: PressureUnits): Float {
        return targetUnit.fromSI(getSIValue())
    }

    override fun getSIValue(): Float {
        return pressure * unit.siConversionFactor
    }
}

abstract class PhysicalUnit<T> {
    abstract fun getValue(targetUnit: T): Float
    abstract fun getSIValue(): Float

}