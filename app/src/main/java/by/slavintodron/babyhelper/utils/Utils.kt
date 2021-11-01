package by.slavintodron.babyhelper.utils

import by.slavintodron.babyhelper.service.FeedingService.Companion.START_TIME
import java.text.SimpleDateFormat
import java.util.*

fun Long.displayTime(): String {
    if (this <= 0L) {
        return START_TIME
    }
    val h = this / 1000 / 3600
    val m = this / 1000 % 3600 / 60
    val s = this / 1000 % 60
    val ms = this % 1000 / 10

    return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
}

private fun displaySlot(count: Long): String {
    return if (count / 10L > 0) {
        "$count"
    } else {
        "0$count"
    }
}

fun GregorianCalendar.toDayOnlyTime(): Long {
    this.set(Calendar.DAY_OF_MONTH, 1)
    this.set(Calendar.MONTH, 1)
    this.set(Calendar.YEAR, 2000)
    return this.timeInMillis
}

fun GregorianCalendar.toDayOnlyDate(): Long {
    this.set(Calendar.HOUR, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 1)
    return this.timeInMillis
}

fun convertLongToDate(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd")
    return format.format(date)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

