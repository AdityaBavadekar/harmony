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

import android.graphics.RectF
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

private fun Double.percent(of: Double): Double = (this * of / 100)

@Preview
@Composable
fun TestChartA(
    lineColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    strokeWidth: Float = 20f,
    barSpacerWidth: Dp = 50.dp,
) {
    val xAxisData = IntRange(1, 12).toList()
    val yAxisData = List(xAxisData.size) { index -> index }
    val yMax = yAxisData.max()
    val yMin = yAxisData.min()
    fun spaceFactor(index: Int) = index * barSpacerWidth.value
    val labelTextStyle = TextStyle.Default.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 8.sp
    )
    val textMeasurer = rememberTextMeasurer()
    val yLabelMaxWidth = textMeasurer.measure(
        yMax.toString(),
        style = labelTextStyle
    ).size.width.dp
    val minHeight = 200.dp

    HarmonyTheme {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(Modifier.padding(8.dp)) {
                Row(Modifier.size(minHeight)) {
                    Box(
                        Modifier
                            .padding(top = 8.dp)
                            .background(backgroundColor)
                            .fillMaxHeight()
                            .width((labelTextStyle.fontSize.value * yMax.toString().length).dp)
                            .drawBehind {

                                if (yAxisData.isEmpty()) return@drawBehind

                                drawBarChartLeftText(
                                    textMeasurer = textMeasurer,
                                    textStyle = labelTextStyle,
                                    (yAxisData.lastIndex + 1).toString(),
                                    yValue = yAxisData
                                        .last()
                                        .toFloat(),
                                    yMax = yMax.toFloat()
                                )

                                val selectedIndices = mutableListOf<Int>()

                                if (yAxisData.size < 2) return@drawBehind

                                if (yAxisData.lastIndex % 2 == 0) {
                                    selectedIndices.add((yAxisData.lastIndex) / 2)
                                } else {
                                    selectedIndices.add(yAxisData.lastIndex / 2)
                                }

                                selectedIndices
                                    .forEach { index ->
                                        val element = yAxisData[index]
                                        drawBarChartLeftText(
                                            textMeasurer = textMeasurer,
                                            textStyle = labelTextStyle,
                                            (index + 1).toString(),
                                            yValue = element.toFloat(),
                                            yMax = yMax.toFloat()
                                        )
                                    }
                            }
                    )

                    Spacer(
                        Modifier
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.onSurface)
                            .width(0.5.dp)
                    )

                    Column(
                        Modifier
                            .clipToBounds()
                            .horizontalScroll(rememberScrollState())
                            .fillMaxHeight()
                            .width(spaceFactor(yAxisData.size).dp)
                    ) {
                        val canvasBounds = remember { RectF() }
                        Box(
                            Modifier
                                .padding(
                                    start = 8.dp,
                                    top = 8.dp
                                )
                                .background(backgroundColor)
                                .fillMaxHeight()
                                .weight(1f)
                                .width(spaceFactor(yAxisData.size).dp)
                                .drawBehind {
                                    yAxisData.forEachIndexed { index, element ->
                                        drawBarChartLine(
                                            lineColor = lineColor,
                                            yValue = element.toFloat(),
                                            yMax = yMax.toFloat(),
                                            strokeWidth = strokeWidth,
                                            spaceFactor = spaceFactor(index)
                                        )
                                        drawBarChartLeftText(
                                            textMeasurer = textMeasurer,
                                            textStyle = labelTextStyle,
                                            (element).toString(),
                                            yValue = element.toFloat(),
                                            yMax = yMax.toFloat(),
                                            xCoordinate = spaceFactor(index) - 7f
                                        )
                                    }
                                }
                        )

                        VerticalSpacer(size = 8.dp)
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .width(spaceFactor(yAxisData.size).dp)
                                .background(MaterialTheme.colorScheme.onSurface)
                                .height(0.5.dp)
                        )
                        Box(
                            Modifier
                                .background(backgroundColor)
                                .height(18.dp)
                                .padding(
                                    start = 8.dp
                                )
                                .fillMaxWidth()
                                .drawBehind {
                                    yAxisData.forEachIndexed { index, element ->
                                        drawBarChartBottomText(
                                            textMeasurer = textMeasurer,
                                            textStyle = labelTextStyle,
                                            (element).toString(),
                                            xCoordinate = spaceFactor(index)
                                        )
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

fun DrawScope.drawBarChartLine(
    lineColor: Color,
    yValue: Float,
    yMax: Float,
    strokeWidth: Float,
    spaceFactor: Float,
) {
    drawLine(
        lineColor,
        start = Offset(spaceFactor, size.height),
        end = Offset(spaceFactor, calculateHeight(yValue, yMax, size.height)),
        cap = StrokeCap.Round,
        strokeWidth = strokeWidth,
    )

}


fun DrawScope.drawBarChartLeftText(
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    labelText: String,
    yValue: Float,
    yMax: Float,
    xCoordinate: Float = 0f
) {
    val calculatedHeight =
        calculateHeight(
            yValue = yValue,
            yMax = yMax,
            columnHeight = size.height
        )
    val yCoordinate = calculatedHeight - textMeasurer.measure("0", style = textStyle).size.height

    drawText(
        textMeasurer = textMeasurer,
        text = labelText,
        topLeft = Offset(xCoordinate, yCoordinate),
        style = textStyle
    )
}

fun DrawScope.drawBarChartBottomText(
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    labelText: String,
    xCoordinate: Float
) {
    drawText(
        textMeasurer = textMeasurer,
        text = labelText,
        topLeft = Offset(xCoordinate - 7f, 0f),
//        Offset(
//            xCoordinate, size.height - textMeasurer.measure("0", style = textStyle).size.height
//        ),
        style = textStyle
    )
}

fun calculateHeight(yValue: Float, yMax: Float, columnHeight: Float): Float {
    val x: Float = if (yValue <= yMax) {
        100f - yValue * 100 / yMax
    } else {
        0f
    }
    return x * columnHeight / 100
}