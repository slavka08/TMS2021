package by.slavintodron.babyhelper.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.slavintodron.babyhelper.db.room.dao.MealDAO
import by.slavintodron.babyhelper.db.room.dao.MealDAO.Companion.APP_DATABASE
import by.slavintodron.babyhelper.entity.MealEntity

@Database(entities = [MealEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun workoutDao(): MealDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java, APP_DATABASE
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


