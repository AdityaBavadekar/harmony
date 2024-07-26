/*
 * Copyright 2024 Aditya Bavadekar
 *
 * Licensed under the Apache License, Version 2.0 (listOf(the "License"));
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

package com.adityabavadekar.harmony.data

import androidx.annotation.DrawableRes
import com.adityabavadekar.harmony.R

enum class WorkoutCategories { AEROBIC, WEIGHT, WATER, BALL, STRETCHING, WINTER, GENERAL, HILL, GYM, OTHER, SPORTS }

enum class WorkoutTypes(
    val category: List<WorkoutCategories>,
    val nameRes: Int = R.string.workout_type, //type: @StringRes
    @DrawableRes val drawableRes: Int = R.drawable.other_excercises
) {
    // All Workouts together come under "ALL" Category 

    TYPE_UNKNOWN(listOf(WorkoutCategories.OTHER), R.string.unknown),
    TYPE_OTHER(listOf(WorkoutCategories.OTHER), R.string.other),

    /* Gym Workouts*/
    TYPE_TREADMILL_RUNNING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.treadmill_running),
    TYPE_PUSH_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.push_ups),
    TYPE_PULL_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.pull_ups),
    TYPE_CRUNCHES(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.crunches),
    TYPE_PLANKS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.planks),
    TYPE_SIT_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.sit_ups),
    TYPE_LUNGES(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.lunges),
    TYPE_SQUATS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.squats),
    TYPE_JUMPING_ROPE(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.jumping_rope),
    TYPE_JUMPING_JACK(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.jumping_jack),
    TYPE_BENCH_PRESS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.bench_press),
    TYPE_WEIGHT_LIFTING(listOf(WorkoutCategories.WEIGHT, WorkoutCategories.GYM), R.string.weight_lifting),

    /* Sports */
    TYPE_BADMINTON(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.badminton),
    TYPE_BASEBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.baseball),
    TYPE_BASKETBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.basketball),
    TYPE_GOLF(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.golf),
    TYPE_HANDBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.handball),
    TYPE_RACQUETBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.racquetball),
    TYPE_ROLLER_HOCKEY(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.roller_hockey),
    TYPE_RUGBY(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.rugby),
    TYPE_SOCCER(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.soccer),
    TYPE_SOFTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.softball),
    TYPE_SQUASH(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.squash),
    TYPE_TABLE_TENNIS(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.table_tennis),
    TYPE_TENNIS(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.tennis),
    TYPE_VOLLEYBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.volleyball),
    TYPE_FOOTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.football),
    TYPE_AMERICAN_FOOTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.american_football),

    /* Sports General*/
    TYPE_BOXING(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.boxing),
    TYPE_CRICKET(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.cricket),
    TYPE_DANCING(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.dancing),

    /* Aerobic Sports Exercises */
    TYPE_BIKING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.biking),
    TYPE_GUIDED_BREATHING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.guided_breathing),
    TYPE_GYMNASTICS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.gymnastics),
    TYPE_STAIR_CLIMBING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.stair_climbing),
    TYPE_ARCHERY(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.archery),
    TYPE_BALLET(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.ballet),

    /* WinterOnly Sports */
    TYPE_ICE_HOCKEY(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.ice_hockey),
    TYPE_ICE_SKATING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.ice_skating),
    TYPE_SKATING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.skating),
    TYPE_SKIING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.skiing),
    TYPE_SNOWBOARDING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.snowboarding),
    TYPE_SNOWSHOEING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.snowshoeing),

    /* WaterOnly Sports */
    TYPE_ROWING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.rowing),
    TYPE_SURFING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.surfing),
    TYPE_SWIMMING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.swimming),
    TYPE_WATER_POLO(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.water_polo),
    TYPE_SCUBA_DIVING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.scuba_diving),
    TYPE_SAILING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.sailing),

    /* Hill Related Sports */
    TYPE_HIKING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.hiking),
    TYPE_MOUNTAIN_BIKING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.mountain_biking),
    TYPE_ROCK_CLIMBING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.rock_climbing),

    /* Common Aerobic Exercises */
    TYPE_WALKING(listOf(WorkoutCategories.AEROBIC), R.string.walking, R.drawable.walking),
    TYPE_RUNNING(listOf(WorkoutCategories.AEROBIC), R.string.running, R.drawable.running),
    TYPE_CYCLING(listOf(WorkoutCategories.AEROBIC), R.string.cycling, R.drawable.cycling),
    TYPE_STRETCHING(listOf(WorkoutCategories.STRETCHING), R.string.stretching),

    /* Yoga Exercises */
    TYPE_YOGA(listOf(WorkoutCategories.GENERAL), R.string.yoga),
}