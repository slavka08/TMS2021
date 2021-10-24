package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.di.module.MealDBRepository
import by.slavintodron.babyhelper.entity.BabyMeal
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.MealType
import by.slavintodron.babyhelper.db.room.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject  constructor(
    @MealDBRepository private val dbRepository: MealRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()

    var meals =MutableLiveData<List<MealEntity>>()
    private set

    fun inserdb(){
        dbRepository.insertMeal(MealEntity(meal = BabyMeal("date", MealType.MILK, 10f)))
    }

    fun getAllMeals(){
        disposable.add(
            dbRepository.getAllMeals().take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mealsData ->
                    meals.postValue(mealsData)
                }, {})
        )
    }
}
