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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabavadekar.harmony.ui.common.TimeUnits
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import java.util.Calendar
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalSnapperApi::class)
@Composable
fun HarmonyPicker(
    itemCount: Int,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    boxSize: Dp = MaterialTheme.typography.bodyLarge.fontSize.value.plus(5).dp,
    width: Dp = 144.dp,
    height: Dp = boxSize * visibleItemsCount,
    infinite: Boolean = true,
    alphaFactor: Float = 1f / (visibleItemsCount - 1),
    selectedItemShape: Shape = RoundedCornerShape(16.dp),
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    selectedItemBorderStroke: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    onSelected: (itemIndex: Int) -> Unit = {},
    content: @Composable BoxScope.(itemIndex: Int) -> Unit,
) {
    val totalItemsCount = itemCount + 2
    val upperHeaderIndex = 0
    val lowerHeaderIndex = totalItemsCount - 1
    val lazyListState = rememberLazyListState(startIndex)
    val isScrollInProgress = lazyListState.isScrollInProgress
    val selectedItemIndex = remember {
        mutableIntStateOf(1)
    }
    val layoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState)

    LaunchedEffect(isScrollInProgress) {
        if (!isScrollInProgress) {
            calculateSnappedItemIndex(
                snapperLayoutInfo = layoutInfo,
                infinite,
                totalItemsCount
            )?.let {
                selectedItemIndex.intValue = it
                onSelected(it)
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = boxSize)
                .size(width, boxSize),
            shape = selectedItemShape,
            color = selectedItemBackgroundColor,
            border = selectedItemBorderStroke,
        ) {}

        LazyColumn(
            modifier = Modifier
                .height(height)
                .width(width),
            state = lazyListState,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        ) {
            items(Integer.MAX_VALUE) { _index ->
                val index = calculateInfiniteIndexValue(_index, totalItemsCount)
                Box(
                    modifier = Modifier
                        .height(boxSize)
                        .width(width)
                        .alpha(
                            calculateAlpha(
                                itemIndex = index,
                                selectedItemIndex = selectedItemIndex.intValue,
                                alphaFactor = alphaFactor,
                                lazyListState = lazyListState,
                                snapperLayoutInfo = layoutInfo
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!infinite) {
                        if (index == upperHeaderIndex || index == lowerHeaderIndex) {
                            Text(text = "______")
                        } else {
                            content(index - 2)
                        }
                    } else {
                        content(index)
                    }
                }
            }
        }
    }
}

private fun calculateInfiniteIndexValue(itemIndex: Int, totalItemCount: Int): Int {
    if (itemIndex < totalItemCount) return itemIndex
    return itemIndex % totalItemCount
}

@OptIn(ExperimentalSnapperApi::class)
private fun calculateSnappedItemIndex(
    snapperLayoutInfo: SnapperLayoutInfo,
    infinite: Boolean,
    totalItemCount: Int,
): Int? {
    var currentItemIndex: Int? = snapperLayoutInfo.currentItem?.index
    if (currentItemIndex != null) {
        if (snapperLayoutInfo.currentItem?.offset == 0) {
            currentItemIndex += 1
        }

        if (!infinite) {
            var corrected = false

            if (currentItemIndex == 0) {
                corrected = true
                currentItemIndex = 1
            }
            if (currentItemIndex == snapperLayoutInfo.totalItemsCount - 1) {
                corrected = true
                currentItemIndex -= 1
            }
            if (!corrected) currentItemIndex -= 1
        } else {
            currentItemIndex = calculateInfiniteIndexValue(currentItemIndex, totalItemCount)
        }
    }

    return currentItemIndex
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun calculateAlpha(
    itemIndex: Int,
    selectedItemIndex: Int,
    alphaFactor: Float = 0.1f,
    lazyListState: LazyListState,
    snapperLayoutInfo: SnapperLayoutInfo,

    ): Float {
    val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(itemIndex).absoluteValue
    val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()

    //    val factor = 1 - (abs(itemIndex - selectedItemIndex) * alphaFactor).toFloat()

    return if (distanceToIndexSnap in 0..viewPortHeight.toInt()) {
        1f - (distanceToIndexSnap / viewPortHeight)
    } else {
        0.2f
    }

//    return 1 - alphaFactor
}

@Composable
fun HarmonyNumberPicker(
    bounds: IntRange,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    getLabel: (Int) -> (String) = { it.toString() },
    fontSize: TextUnit = 32.sp,
    infinite: Boolean = false,
) {
    HarmonyPicker(
        modifier = modifier,
        itemCount = bounds.count(),
        boxSize = (fontSize.value + 10).dp,
        visibleItemsCount = visibleItemsCount
    ) { itemIndex ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getLabel(itemIndex + 1),
                fontSize = fontSize
            )
        }
    }
}

data class DatePickerResult(val year: Int, val month: Int, val date: Int)

@Composable
fun HarmonyDatePicker(
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    onDateChangedListener: (DatePickerResult) -> Unit = {},
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear - 100)..currentYear
    val months = listOf(
        Pair("January", 31),
        Pair("February", 28),
        Pair("March", 31),
        Pair("April", 30),
        Pair("May", 31),
        Pair("June", 30),
        Pair("July", 31),
        Pair("August", 31),
        Pair("September", 30),
        Pair("October", 31),
        Pair("November", 30),
        Pair("December", 31)
    )
    val currentCalendar = Calendar.getInstance()

    val selectedMonthIndex = remember {
        mutableIntStateOf(currentCalendar.get(Calendar.MONTH))
    }
    val selectedDate = remember {
        mutableIntStateOf(currentCalendar.get(Calendar.DAY_OF_MONTH))
    }
    val selectedYear = remember {
        mutableIntStateOf(currentCalendar.get(Calendar.YEAR))
    }

    fun updateCalendar() {
        val result = DatePickerResult(
            selectedYear.intValue,
            selectedMonthIndex.intValue + 1,
            selectedDate.intValue
        )
        onDateChangedListener(result)
    }

    val pickerWidth = (LocalConfiguration.current.screenWidthDp / 4).dp

    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HarmonyPicker(
            modifier = modifier,
            itemCount = months.count(),
            width = pickerWidth.plus(8.dp),
            startIndex = selectedMonthIndex.intValue,
            boxSize = (fontSize.value + 10).dp,
            visibleItemsCount = visibleItemsCount,
            onSelected = {
                selectedMonthIndex.intValue = it
                updateCalendar()
            }
        ) { itemIndex ->
            HarmonyDatePickerItem(itemIndex, getLabel = {
                return@HarmonyDatePickerItem months[it].first
            }, fontSize)
        }
        HarmonyPicker(
            modifier = modifier,
            itemCount = months[selectedMonthIndex.intValue].second,
            width = pickerWidth,
            startIndex = selectedDate.intValue - 1,
            boxSize = (fontSize.value + 10).dp,
            onSelected = {
                selectedDate.intValue = it + 1
                updateCalendar()
            },
            visibleItemsCount = visibleItemsCount
        ) { itemIndex ->
            HarmonyDatePickerItem(
                itemIndex,
                getLabel = { index -> (index + 1).toString() },
                fontSize
            )
        }
        HarmonyPicker(
            modifier = modifier,
            itemCount = years.count(),
            startIndex = years.last - selectedYear.intValue,
            width = pickerWidth,
            boxSize = (fontSize.value + 10).dp,
            onSelected = {
                selectedYear.intValue = years.last - it
                updateCalendar()
            },
            visibleItemsCount = visibleItemsCount
        ) { itemIndex ->
            HarmonyDatePickerItem(
                itemIndex,
                getLabel = { (years.last - it).toString() },
                fontSize
            )
        }
    }
}

@Composable
fun HarmonyDatePickerItem(
    itemIndex: Int,
    getLabel: (Int) -> kotlin.String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getLabel(itemIndex + 1),
            fontSize = fontSize
        )
    }
}


@Composable
fun HarmonyPopupListPicker(
    itemCount: Int,
    getLabel: (Int) -> String,
    isExpanded: Boolean,
    setNotExpanded: () -> Unit,
    onSelected: (itemIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isExpanded,
        onDismissRequest = { setNotExpanded() }) {
        repeat(itemCount) {
            DropdownMenuItem(
                text = {
                    Text(text = getLabel(it))
                },
                onClick = {
                    setNotExpanded()
                    onSelected(it)
                }
            )
        }
    }
}


@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
    var index by mutableIntStateOf(0)
    private var consumed = false
    fun isConsumed() = consumed
    fun consume() {
        consumed = true
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    itemCount: Int,
    getLabel: (Int) -> String,
    modifier: Modifier = Modifier,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % itemCount - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = getLabel((index + 0) % itemCount)

    var addIndex = 0
    if (!state.isConsumed() && state.selectedItem != "") {
        for (i in 0..itemCount) {
            if (getLabel(i) == state.selectedItem) {
                addIndex = i
                break
            }
        }
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)

    val textColor = MaterialTheme.colorScheme.onSurface
    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.30f to textColor.copy(alpha = 0.5f),
            0.5f to textColor,
            0.70f to textColor.copy(alpha = 0.5f),
            1f to Color.Transparent
        )
    }
    LaunchedEffect(key1 = 0) {
        if (!state.isConsumed()) {
            listState.scrollToItem(addIndex + if (addIndex > 0) -1 else 0)
            state.consume()
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                val index = it + 1 % itemCount
                state.selectedItem = getItem(index)
            }

    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier)
                )
            }
        }

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle)
        )

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1))
        )

    }

}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Preview
@Composable
private fun PickerTest() {
    HarmonyTheme {
        Surface {
            Row {
                val state = rememberPickerState()
                Text(text = "${state.selectedItem}|${state.index}")
                Picker(
                    state = state,
                    itemCount = TimeUnits.entries.size,
                    getLabel = { TimeUnits.entries[it].name },
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(0.3f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 32.sp)
                )
            }
        }
    }
}