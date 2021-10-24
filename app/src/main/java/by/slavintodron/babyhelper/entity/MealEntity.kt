package by.slavintodron.babyhelper.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.slavintodron.babyhelper.db.room.dao.MealDAO.Companion.MEALS_TABLE

@Entity(tableName = MEALS_TABLE)
data class MealEntity(
    @PrimaryKey val id: Int? = null,

    @Embedded
    var meal: BabyMeal
)
