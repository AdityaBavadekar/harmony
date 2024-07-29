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

package com.adityabavadekar.harmony.utils

import com.adityabavadekar.harmony.ui.common.HeatUnits
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.MassUnits
import com.adityabavadekar.harmony.ui.common.SpeedUnits
import com.adityabavadekar.harmony.ui.common.TemperatureUnits
import com.adityabavadekar.harmony.ui.common.VolumeUnits
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.PreferencesManager

class UnitPreferences(private val preferencesManager: PreferencesManager) {

    fun weightUnitPreference(): MassUnits {
        preferencesManager.getInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE)
            ?.let { return MassUnits.entries[it] }
        return DEFAULT_WEIGHT_UNIT
    }

    fun heatUnitPreference(): HeatUnits {
        preferencesManager.getInt(PreferencesKeys.HEIGHT_UNIT_PREFERENCE)
            ?.let { return HeatUnits.entries[it] }
        return DEFAULT_HEAT_UNIT
    }

    fun heightUnitPreference(): LengthUnits {
        preferencesManager.getInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE)
            ?.let { return LengthUnits.entries[it] }
        return DEFAULT_HEIGHT_UNIT
    }

    fun temperatureUnitPreference(): TemperatureUnits {
        preferencesManager.getInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE)
            ?.let { return TemperatureUnits.entries[it] }
        return DEFAULT_TEMPERATURE_UNIT
    }

    fun distanceUnitPreference(): LengthUnits {
        preferencesManager.getInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE)
            ?.let { return LengthUnits.entries[it] }
        return DEFAULT_DISTANCE_UNIT
    }

    fun waterIntakeUnitPreference(): VolumeUnits {
        preferencesManager.getInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE)
            ?.let { return VolumeUnits.entries[it] }
        return DEFAULT_VOLUME_UNIT
    }

    fun saveWeightUnitPreference(value: Int) {
        if (value >= MassUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.WEIGHT_UNIT_PREFERENCE, value)
    }

    fun saveHeatUnitPreference(value: Int) {
        if (value >= HeatUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.HEAT_UNIT_PREFERENCE, value)
    }

    fun saveHeightUnitPreference(value: Int) {
        if (value >= LengthUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.HEIGHT_UNIT_PREFERENCE, value)
    }

    fun saveTemperatureUnitPreference(value: Int) {
        if (value >= TemperatureUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.TEMPERATURE_UNIT_PREFERENCE, value)
    }

    fun saveDistanceUnitPreference(value: Int) {
        if (value >= LengthUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.DISTANCE_UNIT_PREFERENCE, value)
    }

    fun saveWaterIntakeUnitPreference(value: Int) {
        if (value >= VolumeUnits.entries.size) throw WrongUnitValueException(value)
        preferencesManager.setInt(PreferencesKeys.WATER_INTAKE_UNIT_PREFERENCE, value)
    }

    private class WrongUnitValueException(value: Int) : IllegalStateException(
        "integer value (${value}) for Unit Preference is not out of enum bounds"
    )


    companion object {
        val DEFAULT_WEIGHT_UNIT = MassUnits.KG
        val DEFAULT_HEAT_UNIT = HeatUnits.CALORIES
        val DEFAULT_HEIGHT_UNIT = LengthUnits.CENTIMETERS
        val DEFAULT_TEMPERATURE_UNIT = TemperatureUnits.CELSIUS
        val DEFAULT_DISTANCE_UNIT = LengthUnits.KILOMETERS
        val DEFAULT_VOLUME_UNIT = VolumeUnits.MILLILITER


        fun getSpeedUnit(distanceUnit: LengthUnits): SpeedUnits {
            return when (distanceUnit) {
                LengthUnits.KILOMETERS -> SpeedUnits.KILOMETERS_PER_HOUR
                LengthUnits.MILES -> SpeedUnits.MILES_PER_HOUR
                else -> SpeedUnits.METERS_PER_SECOND
            }
        }
    }

}