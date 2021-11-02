package by.slavintodron.babyhelper

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContentProviderCompat.requireContext
import by.slavintodron.babyhelper.ui.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class BabyMealsAppWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    fun update(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    }
    companion object {
        private fun getPendingIntent(context: Context, value: Int): PendingIntent {
            val intent = Intent(context, MainActivity::class.java)
            intent.action = MainActivity.UPDATE_WIDGET
            intent.putExtra(MainActivity.WIDGET_TYPE_MEAL, "value")
            intent.putExtra(MainActivity.WIDGET_VOLUME, "value")
            intent.putExtra(MainActivity.WIDGET_DATE, "value")
            return PendingIntent.getActivity(context, value, intent, 0)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    val views = RemoteViews(context.packageName, R.layout.baby_meals_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)
    appWidgetManager.updateAppWidget(appWidgetId, views)

}

