package by.slavintodron.babyhelper.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.ui.MainActivity
import by.slavintodron.babyhelper.utils.displayTime
import kotlinx.coroutines.*

class FeedingService : Service() {

    private var isServiceStarted = false
    private var notificationManager: NotificationManager? = null
    private var job: Job? = null
    private var timer1: Long = 0
    private var timer2: Long = 0
    private var runTimer = true
    private var usedLeftTimer = true
    private val builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_feeding_title))
            .setGroup("Timer")
            .setGroupSummary(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntentOpen())
            .setSilent(true)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .addAction(R.drawable.common_google_signin_btn_icon_dark, getString(R.string.pause),
                getPendingIntentRunToggle())
            .addAction(R.drawable.common_google_signin_btn_icon_dark, getString(R.string.change),
                getPendingIntentToggle())
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processCommand(intent)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun processCommand(intent: Intent?) {
        val x = intent?.action

        when (x ?: INVALID) {
            COMMAND_START -> {
                timer1 = intent?.getLongExtra(LEFT_TIMER, 0) ?: 0
                timer2 = intent?.getLongExtra(RIGHT_TIMER, 0) ?: 0
                val startTime = intent?.extras?.getLong(STARTED_TIMER_TIME_MS) ?: return
                commandStart(startTime)
            }
            COMMAND_TOGGLE_TIMER_STATUS -> toggleTimer()
            COMMAND_TOGGLE_TIMER_SIDE -> toggleTimerSide()
            COMMAND_STOP -> commandStop()
            INVALID -> return
        }
    }

    private fun toggleTimer() {
        runTimer = !runTimer
    }

    private fun toggleTimerSide() {
        usedLeftTimer = !usedLeftTimer
    }

    private fun commandStart(startTime: Long) {
        if (isServiceStarted) {
            return
        }
        try {
            moveToStartedState()
            startForegroundAndShowNotification()
            continueTimer(startTime)
        } finally {
            isServiceStarted = true
        }
    }

    private fun continueTimer(timer: Long) {
        job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                if (runTimer) {
                    updateTimer(usedLeftTimer)
                }
                delay(INTERVAL)
            }
        }
    }

    private fun updateTimer(usedLeftTimer: Boolean) {
        when (usedLeftTimer) {
            true -> {
                timer1 += INTERVAL
                notificationManager?.notify(NOTIFICATION_ID, getNotification(String.format("%s: %s",getString(R.string.left),timer1.displayTime().dropLast(3))))
            }
            else -> {
                timer2 += INTERVAL
                notificationManager?.notify(NOTIFICATION_ID, getNotification(String.format("%s: %s",getString(R.string.right),timer2.displayTime().dropLast(3))))
            }
        }
    }

    private fun commandStop() {
        if (!isServiceStarted) {
            return
        }
        try {
            job?.cancel()
            stopForeground(true)
            stopSelf()
        } finally {
            isServiceStarted = false
        }
    }

    private fun moveToStartedState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, FeedingService::class.java))
        } else {
            startService(Intent(this, FeedingService::class.java))
        }
    }

    private fun startForegroundAndShowNotification() {
        createChannel()
        val notification = getNotification("content")
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun getNotification(content: String) = builder.setContentText(content).build()


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "babyhelper"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName, importance
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    private fun getPendingIntentRunToggle(): PendingIntent? {
        val startIntent = Intent(this, FeedingService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_TOGGLE_TIMER_STATUS)
        startIntent.putExtra(STARTED_TIMER_TIME_MS, 55550545048)
        startIntent.action = COMMAND_TOGGLE_TIMER_STATUS
        return PendingIntent.getService(
            this,
            0, startIntent, FLAG_UPDATE_CURRENT
        )
    }

    private fun getPendingIntentToggle(): PendingIntent? {
        val toggleIntent = Intent(this, FeedingService::class.java)
        toggleIntent.putExtra(STARTED_TIMER_TIME_MS, 55550545048)
        toggleIntent.action = COMMAND_TOGGLE_TIMER_SIDE
        toggleIntent.putExtra(COMMAND_ID, COMMAND_TOGGLE_TIMER_SIDE)
        return PendingIntent.getService(
            this,
            0, toggleIntent, FLAG_UPDATE_CURRENT
        )
    }

    private fun getPendingIntentOpen(): PendingIntent? {
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.action = COMMAND_STOP
        resultIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        resultIntent.putExtra(LEFT_TIMER, timer1)
        resultIntent.putExtra(RIGHT_TIMER, timer2)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this, 0, resultIntent, FLAG_ONE_SHOT)
    }


    companion object {

        const val START_TIME = "00:00:00"
        const val INVALID = "INVALID"
        const val COMMAND_START = "COMMAND_START"
        const val COMMAND_STOP = "COMMAND_STOP"
        const val COMMAND_TOGGLE_TIMER_STATUS = "COMMAND_TOGGLE_TIMER_STATUS"
        const val COMMAND_ID = "COMMAND_ID"
        const val STARTED_TIMER_TIME_MS = "STARTED_TIMER_TIME"
        const val COMMAND_TOGGLE_TIMER_SIDE = "COMMAND_TOGGLE_SIDE"
        const val LEFT_TIMER = "LEFT_TIMER"
        const val RIGHT_TIMER = "RIGHT_TIMER"
        private const val CHANNEL_ID = "Channel_by_sl_p"
        private const val NOTIFICATION_ID = 777
        const val INTERVAL = 1000L
    }
}