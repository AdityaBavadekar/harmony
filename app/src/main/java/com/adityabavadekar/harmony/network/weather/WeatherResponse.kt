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

package com.adityabavadekar.harmony.network.weather

import com.adityabavadekar.harmony.ui.common.Temperature
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    val coordinates: WeatherApiCoord,
    val weather: List<WeatherApiWeatherItem>,
    val main: WeatherApiMain,
    val wind: WeatherApiWind,
    val name: String
)

data class WeatherApiCoord(
    val lon: Double,
    val lat: Double
)

data class WeatherApiWeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) {
    fun getIconUrl(): String {
        return "https://openweathermap.org/img/w/${icon}.png"
    }
}

data class WeatherApiMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
) {
    fun getTemperatureObject(): Temperature {
        return Temperature(temp)
    }
}

data class WeatherApiWind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)