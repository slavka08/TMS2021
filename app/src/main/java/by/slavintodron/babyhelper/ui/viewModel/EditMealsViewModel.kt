package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.db.room.repository.MealRepository
import by.slavintodron.babyhelper.di.module.MealDBRepository
import by.slavintodron.babyhelper.entity.BabyMeal
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class EditMealsViewModel @Inject constructor(
    @MealDBRepository private val dbRepository: MealRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()
    var timerL: Long = 0
    var timerR: Long = 0
    var mealData = MutableLiveData<MealEntity>()
        private set

    var meals = MutableLiveData<List<MealEntity>>()
        private set

    fun setMeal(meal: MealEntity) {
        mealData.value = meal
    }

    fun insertDB() {
        mealData.value?.let { dbRepository.insertMeal(it) }
    }

    fun getMeal(id: Int) {
        disposable.add(dbRepository.getMeal(id).take(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ meal ->
                mealData.postValue(meal)
            }, {})
        )
    }

    fun clear() {
        timerL = 0
        timerR = 0
    }

    fun getAllMeals() {
        disposable.add(
            dbRepository.getAllMeals().take(1)
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
