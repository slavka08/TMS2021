package by.slavintodron.babyhelper.utils

import android.content.Context
import android.graphics.Color
import by.slavintodron.babyhelper.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ChartUtils {
    companion object {

        fun setChartDataCalories(chart: PieChart, leftSeconds: Float, rightSeconds: Float, context: Context) {
            var leftPercent = leftSeconds / ((leftSeconds + rightSeconds) / 100)
            if (leftPercent < 0) leftPercent = 0f
            var textColor = context.resources.getColor(R.color.light_gray_text)
            chart.renderer = PieChartRoundedRenderer(chart, chart.animator, chart.viewPortHandler)
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.isDrawHoleEnabled = true
            chart.setHoleColor(Color.TRANSPARENT)
            chart.holeRadius = 80f
            chart.setTouchEnabled(false)
            chart.setDrawCenterText(true)
            chart.setCenterTextColor(textColor)
            chart.setCenterTextSize(24f)
            chart.setDrawEntryLabels(false)
            chart.setDrawRoundedSlices(true)
            chart.isRotationEnabled = true
            chart.legend.isEnabled = false
            chart.maxAngle = 270f
            chart.rotationAngle = 135f
            chart.setCenterTextOffset(0f, 0f)
            val values = ArrayList<PieEntry>()
            values.add(PieEntry(leftPercent))
            if (leftPercent < 100) values.add(PieEntry(100f - leftPercent))
            val dataSet = PieDataSet(values, "Calories")
            dataSet.sliceSpace = 0f
            dataSet.setColors(intArrayOf(
                getChartColor(),
                R.color.light_gray_text
            ), context)
            val data = PieData(dataSet)
            data.setDrawValues(false)
            chart.data = data
            chart.invalidate()
        }

        private fun getChartColor():Int {
           return R.color.green

        }

        fun styleNutritionChart(chart: PieChart, carbsPercent: Int, fatPercent: Int, proteinPercent: Int, context: Context) {
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.isDrawHoleEnabled = true
            chart.setHoleColor(Color.TRANSPARENT)
            chart.holeRadius = 78f
            chart.transparentCircleRadius = 78f
            chart.setTouchEnabled(false)
            chart.legend.isEnabled = false
            val values = ArrayList<PieEntry>()
            values.add(PieEntry(carbsPercent.toFloat() * 3))
            values.add(PieEntry(fatPercent.toFloat() * 3))

            val dataSet = PieDataSet(values, "Nutrition")
            dataSet.sliceSpace = 3f
            dataSet.setColors(
                    intArrayOf(R.color.green, R.color.green),
                    context
            )
            val data = PieData(dataSet)
            data.setDrawValues(false)
            chart.data = data
            chart.animateY(800)
            chart.invalidate()
        }
    }
}