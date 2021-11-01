package by.slavintodron.babyhelper.db.room.repository

import by.slavintodron.babyhelper.db.room.dao.MealDAO
import by.slavintodron.babyhelper.entity.MealEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MealDB @Inject constructor(private val workoutDao: MealDAO) : MealRepository {

    private val disposable = CompositeDisposable()

    override fun insertMeal(meal: MealEntity) {
        workoutDao.insertWorkOut(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{})
            .add()
    }

    override fun getMeal(id: Int): Flowable<MealEntity> = workoutDao.getById(id)

    override fun getMealsByDate(date: Long): Flowable<List<MealEntity>> = workoutDao.getByDate(date)

    override fun getLastMeal(): Flowable<MealEntity> = workoutDao.getLastMeal()

    override fun getAllMeals(): Flowable<List<MealEntity>> = workoutDao.getAllWorkouts()

    private fun Disposable.add() {
        disposable.add(this)
    }

    fun clear() {
        disposable.clear()
    }
}