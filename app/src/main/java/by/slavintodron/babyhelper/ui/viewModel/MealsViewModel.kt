package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.db.room.repository.MealRepository
import by.slavintodron.babyhelper.di.module.MealDBRepository
import by.slavintodron.babyhelper.entity.BabyMeal
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.MealType
import by.slavintodron.babyhelper.utils.toDayOnlyDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    @MealDBRepository private val dbRepository: MealRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val _calendar = GregorianCalendar()
    val calendar get() = _calendar
    var mealData = MutableLiveData<MealEntity>()
        private set

    var meals = MutableLiveData<List<MealEntity>>()
        private set

    fun nextDate() {
        _calendar.add(Calendar.DATE, 1)
        getMeals()
    }

    fun prevDate() {
        _calendar.add(Calendar.DATE, -1)
        getMeals()
    }

    fun getMeals() {
        disposable.add(
            dbRepository.getMealsByDate(_calendar.toDayOnlyDate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mealsData ->
                    meals.postValue(mealsData)
                }, {})
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
