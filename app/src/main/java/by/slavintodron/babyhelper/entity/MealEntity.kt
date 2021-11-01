package by.slavintodron.babyhelper.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import by.slavintodron.babyhelper.db.room.dao.MealDAO.Companion.MEALS_TABLE

@Entity(tableName = MEALS_TABLE)
data class MealEntity(
    @PrimaryKey val id: Int? = null,
    var dateDay: Long,
    var dateTime: Long,
    @Embedded
    var meal: BabyMeal
)
