package by.slavintodron.babyhelper.db.room.repository

import by.slavintodron.babyhelper.entity.MealEntity
import io.reactivex.Flowable

interface MealRepository {
    fun insertMeal(meal: MealEntity)
    fun getMeal(id: Int = 0): Flowable<MealEntity>
    fun getMealsByDate(date: Long): Flowable<List<MealEntity>>
    fun getLastMeal(): Flowable<MealEntity>
    fun getAllMeals(): Flowable<List<MealEntity>>
    fun deleteMealById(id: Int)
}