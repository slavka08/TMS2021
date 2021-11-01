package by.slavintodron.babyhelper.entity

data class BabyMeal(val timerLeft: Long, val timerRight: Long, val type: MealType, val measUnit: MeasureUnits,val volume: Float, val info: String)
