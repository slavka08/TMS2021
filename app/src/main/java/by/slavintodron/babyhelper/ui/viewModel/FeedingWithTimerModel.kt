package by.slavintodron.babyhelper.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.slavintodron.babyhelper.entity.Breast
import by.slavintodron.babyhelper.service.FeedingService
import kotlinx.coroutines.*

class FeedingWithTimerModel: ViewModel() {
    var timerDataL = MutableLiveData<Long>(0)
    var timerDataR = MutableLiveData<Long>(0)
    private var job: Job? = null

    private var timer1: Long = 0
    private var timer2: Long = 0
    private var _runTimer = false
    val runTimer get() = _runTimer
    private var _usedTimer = MutableLiveData<Breast>()
    val usedTimer get() = _usedTimer

    init {
        _usedTimer.value = Breast.LEFT
        continueTimer(0)
    }

    fun toggleTimerStatus(timer: Breast) {
        when (timer == _usedTimer.value) {
            true -> { _runTimer = !_runTimer }
            false -> { _usedTimer.value = timer
                _runTimer = true }
        }
        _usedTimer.value = timer
    }

    fun continueTimer(timer: Long) {
        job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                if (_runTimer) {
                    _usedTimer.value?.let { updateTimer(it) }
                }
                delay(FeedingService.INTERVAL)
            }
        }
    }

    fun setTimerValue(left:Long?, right:Long?) {
        timerDataL.value = left ?: 0
        timerDataR.value = right ?: 0
    }

    private fun updateTimer(usedLeftTimer: Breast) {
        when (usedLeftTimer) {
            Breast.LEFT -> { timerDataL.postValue(timerDataL.value?.plus(FeedingService.INTERVAL)) }
            Breast.RIGHT -> { timerDataR.postValue(timerDataR.value?.plus(FeedingService.INTERVAL)) }
        }
    }

    companion object {

    }
}