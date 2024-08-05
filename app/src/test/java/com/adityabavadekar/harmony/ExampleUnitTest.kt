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

package com.adityabavadekar.harmony

import com.adityabavadekar.harmony.network.RetrofitInstance
import com.adityabavadekar.harmony.network.weather.WeatherResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private fun weatherInfoString(weatherResponse: WeatherResponse): String {
        val cityName = weatherResponse.name
        val temperature = weatherResponse.main.temp
        val description = weatherResponse.weather.firstOrNull()?.description ?: "[No description]"
        val windSpeed = weatherResponse.wind.speed
        val humidity = weatherResponse.main.humidity

        return """
        City: $cityName
        Temperature: $temperature K
        Weather: $description
        Wind Speed: $windSpeed m/s
        Humidity: $humidity%
    """.trimIndent()
    }


    @Test
    fun does_weatherApi_work_correctly() = runTest {
        val response = RetrofitInstance.weatherApi.getWeather(
            latitude = 18.521428,
            longitude = 73.8544541
        )
        println(response.raw())

        val weatherResponse = response.body()
        if (weatherResponse != null) {
            println(weatherResponse)
            println()
            println(weatherInfoString(weatherResponse))
        } else {
            println("Weather response was null!")
        }

        assertEquals(response.isSuccessful, true)
    }

}