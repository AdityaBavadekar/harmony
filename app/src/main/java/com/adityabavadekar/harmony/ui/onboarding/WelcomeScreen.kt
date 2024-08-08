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

package com.adityabavadekar.harmony.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.animateDpAsState
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun WelcomeScreen(
    onCompleted: () -> Unit = {},
    onPageChanged: (pageIndex: Int) -> Unit = {},
    currentCarouselPageIndex: Int = 0
) {
    val pages = getCarouselPagesList()
    val pagerState =
        rememberPagerState(initialPage = currentCarouselPageIndex) { return@rememberPagerState pages.size }

    @Composable
    fun getContentAtIndex(pageIndex: Int) {
        WelcomeCarouselItem(pages[pageIndex])
    }

    Surface {
        Row {
            Box(Modifier.fillMaxSize()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                ) { pageIndex ->
                    Column { getContentAtIndex(pageIndex) }
                }
                PagerScrollIndicators(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding(),
                    onCompleted = onCompleted,
                    onPageChanged = onPageChanged,
                )
            }
        }
    }
}

@Composable
fun DotIndicators(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val pageCount = pagerState.pageCount
    val animDuration = 500
    Row(
        modifier = modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            val isActive = pagerState.currentPage == pageIndex
            val dotSize by animateDpAsState(
                targetValue = if (isActive) 8.dp else 6.dp,
                animationSpec = tween(durationMillis = animDuration)
            )
            val dotAlpha by animateFloatAsState(
                targetValue = if (isActive) 1f else 0.5f,
                animationSpec = tween(durationMillis = animDuration), label = ""
            )
            DotIndicator(
                isActive = isActive,
                dotSize = dotSize,
                dotAlpha = dotAlpha,
                activeColor = MaterialTheme.colorScheme.onSurface,
                inactiveColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
    dotSize: Dp = 8.dp,
    dotAlpha: Float = 1f
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .alpha(dotAlpha)
            .clip(CircleShape)
            .size(dotSize)
            .background(if (isActive) activeColor else inactiveColor)
    )
}

@Composable
fun PagerScrollIndicators(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onCompleted: () -> Unit,
    onPageChanged: (pageIndex: Int) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .padding(vertical = 18.dp)
            .height(64.dp)
    ) {
        DotIndicators(
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        )
        Button(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 18.dp),
            onClick = {
                val nextPageIndex = pagerState.currentPage + 1
                if (nextPageIndex >= pagerState.pageCount) {
                    onCompleted()
                } else {
                    onPageChanged(nextPageIndex)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(nextPageIndex)
                    }
                }
            }
        ) {
            if (pagerState.currentPage == pagerState.pageCount - 1) {
                Text(text = "Continue")
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                    contentDescription = "Next"
                )
            }
        }
    }
}

@Composable
fun CopyrightAlertDialog(
    copyrightInfo: CopyrightInformation,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Copyright Notice",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = copyrightInfo.text,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "For more information, visit:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = copyrightInfo.url,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}

@Composable
fun WelcomeCarouselItem(carouselPage: CarouselPage, modifier: Modifier = Modifier) {
    val openCopyrightDialog = remember { mutableStateOf(false) }
    val cornerRadius = 0.dp
    val imageSize = 162.dp
    when {
        openCopyrightDialog.value -> {
            carouselPage.copyrightInformation?.let {
                CopyrightAlertDialog(copyrightInfo = it) {
                    openCopyrightDialog.value = false
                }
            }
        }
    }
    Column(
        modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius))
                .background(carouselPage.color ?: MaterialTheme.colorScheme.surface)
                .padding(vertical = 28.dp, horizontal = 32.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = carouselPage.title,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Column {

                Box(Modifier.size(imageSize)) {
                    carouselPage.imagePainter?.let {
                        Image(
                            modifier = Modifier.size(imageSize),
                            painter = it,
                            contentDescription = "Image"
                        )
                    }
                }
                carouselPage.copyrightInformation?.let {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.End),
                        onClick = {
                            openCopyrightDialog.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "info",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
            Text(
                text = carouselPage.heroText,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class CopyrightInformation(val url: String, val text: String)

data class CarouselPage(
    val title: String,
    val imagePainter: Painter?,
    val imageUrl: String?,
    val heroText: String,
    val color: Color?,
    val copyrightInformation: CopyrightInformation? = null,
)

@Composable
private fun getCarouselPagesList() = listOf(
    CarouselPage(
        title = "Welcome to Harmony",
        imagePainter = painterResource(id = R.drawable.yoga_pose),
        imageUrl = null,
        heroText = "Discover a new way to achieve balance and well-being with Harmony. Your personalized guide to a healthier, happier you.",
        color = Color(0xFFB3E5FC),
        copyrightInformation = CopyrightInformation(
            url = "https://www.flaticon.com/free-icon/yoga-pose_2789935",
            text = "Workout icons created by dDara - Flaticon"
        )
    ),
    CarouselPage(
        title = "Personalized Wellness Journey",
        imagePainter = painterResource(id = R.drawable.magic),
        imageUrl = null,
        heroText = "Experience tailored fitness plans and diet suggestions powered by AI. Harmony understands your unique needs and helps you reach your goals.",
        color = Color(0xFF81C784),
        copyrightInformation = CopyrightInformation(
            url = "https://www.flaticon.com/free-icon/magic_867875",
            text = "Spark icons created by Freepik - Flaticon"
        )
    ),
    CarouselPage(
        title = "Track Your Progress",
        imagePainter = painterResource(id = R.drawable.analytics),
        imageUrl = null,
        heroText = "Stay on top of your wellness journey with real-time tracking, insightful statistics, and daily tips. Harmony keeps you motivated and informed.",
        color = Color(0xFFFFF176)
    )
)