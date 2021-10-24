package by.slavintodron.babyhelper.api

import by.slavintodron.babyhelper.entity.weatherEntity.WeatherEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast?&units=metric&appid=291e14673620586c3582b7a53947aa7e")
    fun getWeather(@Query("q") city: String): Call<WeatherEntity>

    companion object {
        val BASE_URL = "https://api.openweathermap.org/"
    }
}