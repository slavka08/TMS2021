package by.slavintodron.babyhelper.di

import android.content.Context
import by.slavintodron.babyhelper.api.WeatherApi
import by.slavintodron.babyhelper.db.room.AppDataBase
import by.slavintodron.babyhelper.db.room.dao.MealDAO
import by.slavintodron.babyhelper.db.room.repository.MealDB
import by.slavintodron.babyhelper.db.room.repository.MealRepository
import by.slavintodron.babyhelper.di.module.MealDBRepository
import by.slavintodron.babyhelper.di.module.Weather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDataBase {
        return AppDataBase.getDataBase(appContext)
    }

    @Singleton
    @Provides
    fun provideWorkoutDAO(db: AppDataBase): MealDAO {
        return db.workoutDao()
    }

    @Singleton
    @Provides
    @MealDBRepository
    fun provideWorkoutDBRepository(dao: MealDAO): MealRepository {
        return MealDB(dao)
    }

    @Singleton
    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideokHttpClient(intercepror: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(intercepror)
            .build()
    }

    @Singleton
    @Provides
    @Weather
    fun provideWeatherApi(okHttp: OkHttpClient): WeatherApi {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WeatherApi.BASE_URL)
            .client(okHttp)
            .build()
        return retrofit.create(WeatherApi::class.java)
    }
}