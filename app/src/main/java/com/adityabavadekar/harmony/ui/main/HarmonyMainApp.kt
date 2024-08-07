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

package com.adityabavadekar.harmony.ui.main

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import com.adityabavadekar.harmony.ui.activity.ActivityScreen
import com.adityabavadekar.harmony.ui.common.CommonMenuActions
import com.adityabavadekar.harmony.ui.common.component.HarmonyBottomNavigationItems
import com.adityabavadekar.harmony.ui.common.component.HarmonyNavigationBar
import com.adityabavadekar.harmony.ui.common.component.HarmonyTopAppBar
import com.adityabavadekar.harmony.ui.home.HomeScreen
import com.adityabavadekar.harmony.ui.home.HomeScreenUiState
import com.adityabavadekar.harmony.ui.profile.ProfileScreen

const val HOME_ROUTE: String = "home_route"
private const val HOME_DEEP_LINK_URI_PATTERN =
    "https://www.harmony.adityabavadekar.com/home"

const val ACTIVITY_ROUTE: String = "activity_route"
private const val ACTIVITY_DEEP_LINK_URI_PATTERN =
    "https://www.harmony.adityabavadekar.com/activity"

const val PROFILE_ROUTE: String = "profile_route"
private const val PROFILE_DEEP_LINK_URI_PATTERN =
    "https://www.harmony.adityabavadekar.com/profile"

@Composable
private fun MainNavHost(
    mainAppState: HarmonyMainAppState,
    startDestination: String = HOME_ROUTE,
    onAddNewClicked: (WorkoutTypes) -> Unit = {},
    workoutsData: State<List<WorkoutSummaryRecord>>,
    accountData: State<UserRecord?>,
    homeScreenUiState: State<HomeScreenUiState>,
    onActivityItemClicked: (id: Long, isOngoing: Boolean) -> Unit
) {
    val navHostController = mainAppState.navController
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(
            route = HOME_ROUTE,
            deepLinks = listOf(
                navDeepLink { uriPattern = HOME_DEEP_LINK_URI_PATTERN }
            ),
            enterTransition = {
                slideInHorizontally()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            HomeScreen(
                onAddNewClicked = onAddNewClicked,
                homeScreenUiState = homeScreenUiState.value
            )
        }
        composable(
            route = ACTIVITY_ROUTE,
            deepLinks = listOf(
                navDeepLink { uriPattern = ACTIVITY_DEEP_LINK_URI_PATTERN }
            ),
            enterTransition = {
                slideInHorizontally()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            ActivityScreen(workoutsData = workoutsData.value, onClick = onActivityItemClicked)
        }
        composable(
            route = PROFILE_ROUTE,
            deepLinks = listOf(
                navDeepLink { uriPattern = PROFILE_DEEP_LINK_URI_PATTERN }
            ),
            enterTransition = {
                slideInHorizontally()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            ProfileScreen(account = accountData.value)
        }
    }
}

fun NavController.navigateHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)
fun NavController.navigateActivity(navOptions: NavOptions) = navigate(ACTIVITY_ROUTE, navOptions)
fun NavController.navigateProfile(navOptions: NavOptions) = navigate(PROFILE_ROUTE, navOptions)

@Preview
@Composable
fun HarmonyMainApp(
    mainAppState: HarmonyMainAppState = rememberHarmonyMainAppState(),
    navigationToSettings: () -> Unit = {},
    onAddNewClicked: (WorkoutTypes) -> Unit = {},
    workoutsData: State<List<WorkoutSummaryRecord>> = mutableStateOf(listOf()),
    accountData: State<UserRecord?> = mutableStateOf(null),
    homeScreenUiState: State<HomeScreenUiState> = mutableStateOf(HomeScreenUiState()),
    onActivityItemClicked: (id: Long, isOngoing: Boolean) -> Unit = { _, _ -> }
) {
    HarmonyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            bottomBar = {
                HarmonyNavigationBar {
                    HarmonyBottomNavigationItems(
                        destinations = mainAppState.topLevelDestinations,
                        onSelectionChanged = { mainAppState.navigateToTopLevelDestination(it) }
                    )
                }
            },
            topBar = {
                if (mainAppState.currentTopLevelDestination?.showTopAppBar != false) {
                    HarmonyTopAppBar(
                        titleRes = mainAppState.currentTopLevelDestination?.titleTextId
                            ?: R.string.empty_string,
                        navigationIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = stringResource(R.string.app_name),
                            )
                        },
                        onNavigationIconClicked = { /*TODO*/ },
                        actionIcons = listOf(
                            CommonMenuActions.settings()
                        ),
                        onActionIconClicked = { index ->
                            if (index == 0) navigationToSettings()
                        },
                        centeredTitle = mainAppState.currentTopLevelDestination?.centeredTitle
                            ?: false
                    )
                }
            }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Box {
                    MainNavHost(
                        mainAppState = mainAppState,
                        onAddNewClicked = onAddNewClicked,
                        workoutsData = workoutsData,
                        accountData = accountData,
                        onActivityItemClicked = onActivityItemClicked,
                        homeScreenUiState = homeScreenUiState
                    )
                }
            }
        }
    }
}

@Composable
fun HarmonyBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}
