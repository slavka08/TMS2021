package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.api.WeatherApi
import by.slavintodron.babyhelper.di.module.Weather
import by.slavintodron.babyhelper.entity.weatherEntity.WeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(@Weather private val weatherApi: WeatherApi): ViewModel() {
    private val _error = MutableLiveData<String>()
    val error get() = _error
    private val _weather = MutableLiveData<WeatherEntity>()
    val weather get() = _weather

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
}