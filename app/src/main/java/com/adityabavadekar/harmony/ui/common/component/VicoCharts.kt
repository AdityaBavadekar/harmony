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

package com.adityabavadekar.harmony.ui.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ThemePreviews
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.VicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.AutoScrollCondition
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel

@Composable
fun PreviewSurface(content: @Composable () -> Unit) {

    HarmonyTheme {
        val vicoTheme = VicoTheme(
            candlestickCartesianLayerColors =
            VicoTheme.CandlestickCartesianLayerColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.tertiary,
            ),
            columnCartesianLayerColors =
            MaterialTheme.colorScheme.run { listOf(primary, secondary, tertiary) },
            lineColor = MaterialTheme.colorScheme.outline,
            textColor = MaterialTheme.colorScheme.onSurface,
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(Modifier.padding(8.dp)) {
                content()
            }
        }
    }
//    HarmonyTheme {
//        ProvideVicoTheme(
//            theme = rememberM3VicoTheme(
//                candlestickCartesianLayerColors = VicoTheme.CandlestickCartesianLayerColors(
//                    MaterialTheme.colorScheme.primaryContainer,
//                    MaterialTheme.colorScheme.primary,
//                    Color.Red,
//                ),
//                textColor = MaterialTheme.colorScheme.primaryContainer,
//                lineColor = MaterialTheme.colorScheme.primary
//            )
//        ) {
//            Surface (
//                color = MaterialTheme.colorScheme.surface
//            ){
//                content()
//            }
//        }
//    }
}

private fun getMediumColumnModel(): CartesianChartModel {
    val x = (1..100).toList()
    val y = x.map { it * it }
    return CartesianChartModel(
        ColumnCartesianLayerModel.build {
            series(x, y)
        }
    )
}

private val mediumLineModel =
    CartesianChartModel(LineCartesianLayerModel.build {
        series(
            1,
            2,
            4,
            8,
            3,
            10,
            4,
            7,
            2,
            6,
            4,
            8
        )
    })

@Composable
fun DefaultColumnChart(
    model: CartesianChartModel,
    scrollable: Boolean = true,
    initialScroll: Scroll.Absolute = Scroll.Absolute.Start,
    autoScrollCondition: AutoScrollCondition = AutoScrollCondition.Never,
) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = rememberStartAxis(
                line = rememberLineComponent(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                label = rememberTextComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            ),
            bottomAxis = rememberBottomAxis(
                line = rememberLineComponent(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                label = rememberTextComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            ),
        ),
        model = model,
        scrollState =
        rememberVicoScrollState(
            scrollEnabled = scrollable,
            initialScroll = initialScroll,
            autoScrollCondition = autoScrollCondition,
        ),
    )
}

@ThemePreviews
@Composable
private fun DefaultColumnChartLongScrollable() {
    PreviewSurface {
        DefaultColumnChart(model = getMediumColumnModel())
    }
}

//@ThemePreviews
//@Composable
//private fun DefaultColumnChartLongScrollableEnd() {
//    PreviewSurface {
//        DefaultColumnChart(model = getMediumColumnModel(), initialScroll = Scroll.Absolute.End)
//    }
//}
//
//@ThemePreviews
//@Composable
//private fun DefaultColumnChartLongNonScrollable() {
//    PreviewSurface {
//        DefaultColumnChart(model = getMediumColumnModel(), scrollable = false)
//    }
//}

@Composable
fun DefaultLineChart(
    model: CartesianChartModel,
    scrollable: Boolean = true,
    initialScroll: Scroll.Absolute = Scroll.Absolute.Start,
) {
    PreviewSurface {
        CartesianChartHost(
            chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
            ),
            model = model,
            scrollState = rememberVicoScrollState(scrollable, initialScroll),
        )
    }
}

//@ThemePreviews
@Composable
private fun DefaultLineChartLongScrollable() {
    DefaultLineChart(model = mediumLineModel)
}

//@ThemePreviews
//@Composable
//private fun DefaultLineChartLongScrollableEnd() {
//    DefaultLineChart(model = mediumLineModel, initialScroll = Scroll.Absolute.End)
//}
//
//@ThemePreviews
//@Composable
//private fun DefaultLineChartLongNonScrollable() {
//    DefaultLineChart(model = mediumLineModel, scrollable = false)
//}