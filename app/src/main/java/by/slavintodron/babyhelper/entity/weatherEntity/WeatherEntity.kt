package by.slavintodron.babyhelper.entity.weatherEntity

import by.slavintodron.babyhelper.entity.weatherEntity.City
import by.slavintodron.babyhelper.entity.weatherEntity.WeatherData

data class WeatherEntity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherData>,
    val message: Int
)