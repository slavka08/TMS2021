package by.slavintodron.babyhelper.entity

import by.slavintodron.babyhelper.R

enum class MealType(val textResId: Int, val imgResId: Int) {
    MILK(R.string.milk, R.drawable.ic_food_milk),
    PORRIDGE(R.string.porridge, R.drawable.ic_casha),
    MIXTURE(R.string.mixture, R.drawable.ic_food_baby_botle),
    BREAST_FEEDING(R.string.breast_feeding, R.drawable.ic_food_baby),
    VEGETABLES(R.string.vegetables, R.drawable.ic_food_vegetables),
    FRUITS(R.string.fruits, R.drawable.ic_food_apple)
}