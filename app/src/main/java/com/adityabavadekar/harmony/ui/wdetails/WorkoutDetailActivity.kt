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

package com.adityabavadekar.harmony.ui.wdetails

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.common.component.AnimationComponents
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.UiState
import com.adityabavadekar.harmony.utils.UnitPreferences
import com.adityabavadekar.harmony.utils.asHarmonyApp
import com.adityabavadekar.harmony.utils.preferences.preferencesManager
import com.adityabavadekar.harmony.utils.withIOContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private var recordId: Long? = null
    private var _uiState: MutableStateFlow<UiState<WorkoutRecord>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<WorkoutRecord>> = _uiState.asStateFlow()
    private var newlyCreated: Boolean = false

    fun setRecordId(id: Long) = viewModelScope.launch {
        recordId = id
        withIOContext {
            val record = workoutsRepository.getWorkoutRecord(recordId = id)
            Log.d(TAG, "loaded: $record")
            if (newlyCreated) delay(1500) // Simulate delay for now
            if (record != null) {
                _uiState.value = UiState.Success(record)
            } else {
                _uiState.value = UiState.Error(NullPointerException("Record with id=$id not found"))
            }
        }
    }

    fun setIsNew(v: Boolean) {
        newlyCreated = v
    }

    companion object {
        private const val TAG = "[WorkoutDetailViewModel]"
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutDetailViewModel::class.java)) {
                val workoutsRepository = application.asHarmonyApp().getWorkoutRepository()
                return WorkoutDetailViewModel(workoutsRepository = workoutsRepository) as T
            }
            throw IllegalArgumentException()
        }
    }
}

class WorkoutDetailActivity : ComponentActivity() {

    private val viewModel: WorkoutDetailViewModel by viewModels {
        WorkoutDetailViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recordId = intent.getLongExtra(INTENT_EXTRA_LONG_ID, -1)
        val newlyCreated = intent.getBooleanExtra(INTENT_EXTRA_IS_NEW, false)

        if (recordId == -1L) {
            Log.e(TAG, "onCreate: Record id not provided intent.extras!!")
            throw IllegalStateException("Record id not provided intent.extras!!")
        }

        Log.d(TAG, "onCreate: Got id! ($recordId)")
        val unitPreferences = UnitPreferences(preferencesManager)
        viewModel.setIsNew(newlyCreated)
        viewModel.setRecordId(recordId)

        setContent {
            HarmonyTheme {
                when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
                    is UiState.Loading -> {
                        AnimationComponents.LoadingArcAnimation()
                    }

                    is UiState.Success -> {
                        WorkoutDetailsScreen(
                            workoutRecord = uiState.data,
                            physicalUnitPreferences = unitPreferences.getPhysicalUnitPreferences()
                        )
                    }

                    is UiState.Error -> {
                        Log.e(TAG, "onCreate: Err!", uiState.exception)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "[WorkoutDetailActivity]"
        const val INTENT_EXTRA_LONG_ID = "long_record_id"
        const val INTENT_EXTRA_IS_NEW = "record_is_new"
    }

}