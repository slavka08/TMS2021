package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.api.WeatherApi
import by.slavintodron.babyhelper.db.room.repository.MealRepository
import by.slavintodron.babyhelper.di.module.MealDBRepository
import by.slavintodron.babyhelper.di.module.Weather
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.weatherEntity.WeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    @Weather private val weatherApi: WeatherApi,
    @MealDBRepository private val dbRepository: MealRepository
): ViewModel() {
    private val disposable = CompositeDisposable()
    private val _error = MutableLiveData<String>()
    val error get() = _error
    private val _weather = MutableLiveData<WeatherEntity>()
    val weather get() = _weather
    var mealData = MutableLiveData<MealEntity>()
        private set

    fun getWeather(city: String) {
        weatherApi.getWeather(city).enqueue(object : Callback<WeatherEntity> {
            override fun onResponse(
                call: Call<WeatherEntity>,
                response: Response<WeatherEntity>
            ) {
                _weather.value = response.body()
            }

            override fun onFailure(call: Call<WeatherEntity>, t: Throwable) {
                _error.value = t.localizedMessage
            }
        })
    }

    fun getLastMeal() {
        disposable.add(dbRepository.getLastMeal().take(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ meal ->
                mealData.postValue(meal)
            }, {})
        )
    }
}