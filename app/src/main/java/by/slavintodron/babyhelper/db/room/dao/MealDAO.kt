package by.slavintodron.babyhelper.db.room.dao

import androidx.room.*
import by.slavintodron.babyhelper.entity.MealEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MealDAO {

    @Query("SELECT * FROM $MEALS_TABLE")
    fun getAllWorkouts(): Flowable<List<MealEntity>>

    @Query("SELECT * FROM $MEALS_TABLE WHERE id = :recordId")
    fun getById(recordId: Int): Flowable<MealEntity>

    @Query("SELECT * FROM $MEALS_TABLE WHERE dateDay = :day")
    fun getByDate(day: Long): Flowable<List<MealEntity>>

    @Query("SELECT * FROM $MEALS_TABLE ORDER BY $MEAL_TABLE_ID_FIELD DESC LIMIT 1")
    fun getLastMeal(): Flowable<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkOut(meal: MealEntity): Completable

    companion object {
        const val MEAL_TABLE_ID_FIELD = "id"
        const val APP_DATABASE = "slavintodronbabyhelper"
        const val MEALS_TABLE = "meals_table"
    }
}
