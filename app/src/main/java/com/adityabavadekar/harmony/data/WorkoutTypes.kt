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
import com.adityabavadekar.harmony.utils.CaloriesCalculator

enum class WorkoutCategories { AEROBIC, WEIGHT, WATER, BALL, STRETCHING, WINTER, GENERAL, HILL, GYM, OTHER, SPORTS }

enum class WorkoutTypes(
    val category: List<WorkoutCategories>,
    val nameRes: Int = R.string.workout_type,
    val metValue : Double = MetValues.UNITY_VALUE,
    @DrawableRes val drawableRes: Int = R.drawable.other_excercises
) {
    // All Workouts together come under "ALL" Category 

    TYPE_UNKNOWN(listOf(WorkoutCategories.OTHER), R.string.unknown, MetValues.UNITY_VALUE),
    TYPE_OTHER(listOf(WorkoutCategories.OTHER), R.string.other, MetValues.UNITY_VALUE),

    /* Gym Workouts*/
    TYPE_TREADMILL_RUNNING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.treadmill_running, MetValues.TREADMILL_RUNNING),
    TYPE_PUSH_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.push_ups, MetValues.PUSH_UPS),
    TYPE_PULL_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.pull_ups, MetValues.PULL_UPS),
    TYPE_CRUNCHES(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.crunches, MetValues.CRUNCHES),
    TYPE_PLANKS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.planks, MetValues.PLANKS),
    TYPE_SIT_UPS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.sit_ups, MetValues.SIT_UPS),
    TYPE_LUNGES(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.lunges, MetValues.LUNGES),
    TYPE_SQUATS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.squats, MetValues.SQUATS),
    TYPE_JUMPING_ROPE(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.jumping_rope, MetValues.JUMPING_ROPE),
    TYPE_JUMPING_JACK(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.jumping_jack, MetValues.JUMPING_JACK),
    TYPE_BENCH_PRESS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.GYM), R.string.bench_press, MetValues.BENCH_PRESS),
    TYPE_WEIGHT_LIFTING(listOf(WorkoutCategories.WEIGHT, WorkoutCategories.GYM), R.string.weight_lifting, MetValues.WEIGHT_LIFTING),

    /* Sports */
    TYPE_BADMINTON(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.badminton, MetValues.BADMINTON),
    TYPE_BASEBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.baseball, MetValues.BASEBALL),
    TYPE_BASKETBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.basketball, MetValues.BASKETBALL),
    TYPE_GOLF(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.golf, MetValues.GOLF),
    TYPE_HANDBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.handball, MetValues.HANDBALL),
    TYPE_RACQUETBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.racquetball, MetValues.RACQUETBALL),
    TYPE_ROLLER_HOCKEY(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.roller_hockey, MetValues.ROLLER_HOCKEY),
    TYPE_RUGBY(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.rugby, MetValues.RUGBY),
    TYPE_SOCCER(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.soccer, MetValues.SOCCER),
    TYPE_SOFTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.softball, MetValues.SOFTBALL),
    TYPE_SQUASH(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.squash, MetValues.SQUASH),
    TYPE_TABLE_TENNIS(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.table_tennis, MetValues.TABLE_TENNIS),
    TYPE_TENNIS(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.tennis, MetValues.TENNIS),
    TYPE_VOLLEYBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.volleyball, MetValues.VOLLEYBALL),
    TYPE_FOOTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.football, MetValues.FOOTBALL),
    TYPE_AMERICAN_FOOTBALL(listOf(WorkoutCategories.BALL, WorkoutCategories.SPORTS), R.string.american_football, MetValues.AMERICAN_FOOTBALL),

    /* Sports General*/
    TYPE_BOXING(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.boxing, MetValues.BOXING),
    TYPE_CRICKET(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.cricket, MetValues.CRICKET),
    TYPE_DANCING(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.dancing, MetValues.DANCING),

    /* Aerobic Sports Exercises */
    TYPE_BIKING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.biking, MetValues.BIKING),
    TYPE_GUIDED_BREATHING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.guided_breathing, MetValues.GUIDED_BREATHING),
    TYPE_GYMNASTICS(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.gymnastics, MetValues.GYMNASTICS),
    TYPE_STAIR_CLIMBING(listOf(WorkoutCategories.AEROBIC, WorkoutCategories.SPORTS), R.string.stair_climbing, MetValues.STAIR_CLIMBING),
    TYPE_ARCHERY(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.archery, MetValues.ARCHERY),
    TYPE_BALLET(listOf(WorkoutCategories.GENERAL, WorkoutCategories.SPORTS), R.string.ballet, MetValues.BALLET),

    /* WinterOnly Sports */
    TYPE_ICE_HOCKEY(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.ice_hockey, MetValues.ICE_HOCKEY),
    TYPE_ICE_SKATING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.ice_skating, MetValues.ICE_SKATING),
    TYPE_SKATING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.skating, MetValues.SKATING),
    TYPE_SKIING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.skiing, MetValues.SKIING),
    TYPE_SNOWBOARDING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.snowboarding, MetValues.SNOWBOARDING),
    TYPE_SNOWSHOEING(listOf(WorkoutCategories.WINTER, WorkoutCategories.SPORTS), R.string.snowshoeing, MetValues.SNOWSHOEING),

    /* WaterOnly Sports */
    TYPE_ROWING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.rowing, MetValues.ROWING),
    TYPE_SURFING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.surfing, MetValues.SURFING),
    TYPE_SWIMMING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.swimming, MetValues.SWIMMING),
    TYPE_WATER_POLO(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.water_polo, MetValues.WATER_POLO),
    TYPE_SCUBA_DIVING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.scuba_diving, MetValues.SCUBA_DIVING),
    TYPE_SAILING(listOf(WorkoutCategories.WATER, WorkoutCategories.SPORTS), R.string.sailing, MetValues.SAILING),

    /* Hill Related Sports */
    TYPE_HIKING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.hiking, MetValues.HIKING),
    TYPE_MOUNTAIN_BIKING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.mountain_biking, MetValues.MOUNTAIN_BIKING),
    TYPE_ROCK_CLIMBING(listOf(WorkoutCategories.HILL, WorkoutCategories.SPORTS), R.string.rock_climbing, MetValues.ROCK_CLIMBING),

    /* Common Aerobic Exercises */
    TYPE_WALKING(listOf(WorkoutCategories.AEROBIC), R.string.walking, MetValues.WALKING, R.drawable.walking),
    TYPE_RUNNING(listOf(WorkoutCategories.AEROBIC), R.string.running, MetValues.RUNNING, R.drawable.running),
    TYPE_CYCLING(listOf(WorkoutCategories.AEROBIC), R.string.cycling, MetValues.CYCLING, R.drawable.cycling),
    TYPE_STRETCHING(listOf(WorkoutCategories.STRETCHING), R.string.stretching, MetValues.STRETCHING),

    /* Yoga Exercises */
    TYPE_YOGA(listOf(WorkoutCategories.GENERAL), R.string.yoga, MetValues.YOGA);

    class MetValues {
        companion object {
            const val UNITY_VALUE = 1.0
            const val TREADMILL_RUNNING = 9.8
            const val PUSH_UPS = 8.0
            const val PULL_UPS = 8.0
            const val CRUNCHES = 4.0
            const val PLANKS = 4.0
            const val SIT_UPS = 6.0
            const val LUNGES = 6.0
            const val SQUATS = 5.0
            const val JUMPING_ROPE = 10.0
            const val JUMPING_JACK = 8.0
            const val BENCH_PRESS = 6.0
            const val WEIGHT_LIFTING = 3.0
            const val BADMINTON = 4.5
            const val BASEBALL = 5.0
            const val BASKETBALL = 8.0
            const val GOLF = 4.3
            const val HANDBALL = 12.0
            const val RACQUETBALL = 7.0
            const val ROLLER_HOCKEY = 10.0
            const val RUGBY = 6.3
            const val SOCCER = 10.0
            const val SOFTBALL = 5.0
            const val SQUASH = 12.0
            const val TABLE_TENNIS = 4.0
            const val TENNIS = 7.3
            const val VOLLEYBALL = 3.0
            const val FOOTBALL = 10.3
            const val AMERICAN_FOOTBALL = 8.0
            const val BOXING = 12.0
            const val CRICKET = 4.8
            const val DANCING = 5.5
            const val BIKING = 7.5
            const val GUIDED_BREATHING = 1.0
            const val GYMNASTICS = 4.0
            const val STAIR_CLIMBING = 9.0
            const val ARCHERY = 3.0
            const val BALLET = 6.0
            const val ICE_HOCKEY = 8.0
            const val ICE_SKATING = 7.0
            const val SKATING = 7.0
            const val SKIING = 7.0
            const val SNOWBOARDING = 6.0
            const val SNOWSHOEING = 8.0
            const val ROWING = 6.0
            const val SURFING = 3.0
            const val SWIMMING = 8.0
            const val WATER_POLO = 10.0
            const val SCUBA_DIVING = 7.0
            const val SAILING = 3.0
            const val HIKING = 6.0
            const val MOUNTAIN_BIKING = 8.5
            const val ROCK_CLIMBING = 8.0
            const val WALKING = 3.5
            const val RUNNING = 11.0
            const val CYCLING = 8.0
            const val STRETCHING = 2.3
            const val YOGA = 3.3
        }
    }

}