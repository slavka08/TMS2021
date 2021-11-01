package by.slavintodron.babyhelper.utils

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class PieChartRoundedRenderer(val chart: PieChart,
                              val animator: ChartAnimator,
                              val viewPortHandler: ViewPortHandler
): PieChartRenderer(chart, animator, viewPortHandler) {

    private val mPathBuffer = Path()
    private val mInnerRectBuffer = RectF()

    override fun drawDataSet(c: Canvas?, dataSet: IPieDataSet?) {

        var angle = 0f
        val rotationAngle = mChart.rotationAngle

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val circleBox = mChart.circleBox

        val entryCount = dataSet!!.entryCount
        val drawAngles = mChart.drawAngles
        val center = mChart.centerCircleBox
        val radius = mChart.radius
        val drawInnerArc = mChart.isDrawHoleEnabled && !mChart.isDrawSlicesUnderHoleEnabled
        val userInnerRadius = if (drawInnerArc) radius * (mChart.holeRadius / 100f) else 0f
        val roundedRadius = (radius - radius * mChart.holeRadius / 100f) / 2f
        val roundedCircleBox = RectF()
        val drawRoundedSlices = drawInnerArc && mChart.isDrawRoundedSlicesEnabled

        var visibleAngleCount = 0
        for (j in 0 until entryCount) {
            // draw only if the value is greater than zero
            if (Math.abs(dataSet.getEntryForIndex(j).y) > Utils.FLOAT_EPSILON) {
                visibleAngleCount++
            }
        }

        val sliceSpace = if (visibleAngleCount <= 1) 0f else getSliceSpace(dataSet)

        for (j in 0 until entryCount) {
            val sliceAngle = drawAngles[j]
            var innerRadius = userInnerRadius
            val e: Entry = dataSet.getEntryForIndex(j)

            // draw only if the value is greater than zero
            if (Math.abs(e.y) <= Utils.FLOAT_EPSILON) {
                angle += sliceAngle * phaseX
                continue
            }

            // Don't draw if it's highlighted, unless the chart uses rounded slices
            if (mChart.needsHighlight(j) && !drawRoundedSlices) {
                angle += sliceAngle * phaseX
                continue
            }
            val accountForSliceSpacing = sliceSpace > 0f && sliceAngle <= 180f
            mRenderPaint.color = dataSet.getColor(j)
            val sliceSpaceAngleOuter = if (visibleAngleCount == 1) 0f else sliceSpace / (Utils.FDEG2RAD * radius)
            val startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2f) * phaseY
            var sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY
            if (sweepAngleOuter < 0f) {
                sweepAngleOuter = 0f
            }
            mPathBuffer.reset()
            if (drawRoundedSlices) {
                val x = center.x + (radius - roundedRadius) * Math.cos((startAngleOuter * Utils.FDEG2RAD).toDouble())
                    .toFloat()
                val y = center.y + (radius - roundedRadius) * Math.sin((startAngleOuter * Utils.FDEG2RAD).toDouble())
                    .toFloat()
                roundedCircleBox[x - roundedRadius, y - roundedRadius, x + roundedRadius] = y + roundedRadius
            }
            val arcStartPointX = center.x + radius * Math.cos((startAngleOuter * Utils.FDEG2RAD).toDouble())
                .toFloat()
            val arcStartPointY = center.y + radius * Math.sin((startAngleOuter * Utils.FDEG2RAD).toDouble())
                .toFloat()
            if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                // Android is doing "mod 360"
                mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW)
            } else {
                if (drawRoundedSlices) {
                    mPathBuffer.arcTo(roundedCircleBox, startAngleOuter + 180, 180f)
                }
                mPathBuffer.arcTo(
                    circleBox,
                    startAngleOuter,
                    sweepAngleOuter
                )
            }

            // API < 21 does not receive floats in addArc, but a RectF
            mInnerRectBuffer[center.x - innerRadius, center.y - innerRadius, center.x + innerRadius] =
                center.y + innerRadius
            if (drawInnerArc && (innerRadius > 0f || accountForSliceSpacing)) {
                if (accountForSliceSpacing) {
                    var minSpacedRadius = calculateMinimumRadiusForSpacedSlice(
                        center, radius,
                        sliceAngle * phaseY,
                        arcStartPointX, arcStartPointY,
                        startAngleOuter,
                        sweepAngleOuter
                    )
                    if (minSpacedRadius < 0f) minSpacedRadius = -minSpacedRadius
                    innerRadius = Math.max(innerRadius, minSpacedRadius)
                }
                val sliceSpaceAngleInner =
                    if (visibleAngleCount == 1 || innerRadius == 0f) 0f else sliceSpace / (Utils.FDEG2RAD * innerRadius)
                val startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2f) * phaseY
                var sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY
                if (sweepAngleInner < 0f) {
                    sweepAngleInner = 0f
                }
                val endAngleInner = startAngleInner + sweepAngleInner
                if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                    // Android is doing "mod 360"
                    mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW)
                } else {
                    if (drawRoundedSlices) {
                        val x =
                            center.x + (radius - roundedRadius) * Math.cos((endAngleInner * Utils.FDEG2RAD).toDouble())
                                .toFloat()
                        val y =
                            center.y + (radius - roundedRadius) * Math.sin((endAngleInner * Utils.FDEG2RAD).toDouble())
                                .toFloat()
                        roundedCircleBox[x - roundedRadius, y - roundedRadius, x + roundedRadius] = y + roundedRadius
                        mPathBuffer.arcTo(roundedCircleBox, endAngleInner, 180f)
                    } else mPathBuffer.lineTo(
                        center.x + innerRadius * Math.cos((endAngleInner * Utils.FDEG2RAD).toDouble())
                            .toFloat(),
                        center.y + innerRadius * Math.sin((endAngleInner * Utils.FDEG2RAD).toDouble())
                            .toFloat()
                    )
                    mPathBuffer.arcTo(
                        mInnerRectBuffer,
                        endAngleInner,
                        -sweepAngleInner
                    )
                }
            } else {
                if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                    if (accountForSliceSpacing) {
                        val angleMiddle = startAngleOuter + sweepAngleOuter / 2f
                        val sliceSpaceOffset = calculateMinimumRadiusForSpacedSlice(
                            center,
                            radius,
                            sliceAngle * phaseY,
                            arcStartPointX,
                            arcStartPointY,
                            startAngleOuter,
                            sweepAngleOuter
                        )
                        val arcEndPointX = center.x +
                                sliceSpaceOffset * Math.cos((angleMiddle * Utils.FDEG2RAD).toDouble())
                            .toFloat()
                        val arcEndPointY = center.y +
                                sliceSpaceOffset * Math.sin((angleMiddle * Utils.FDEG2RAD).toDouble())
                            .toFloat()
                        mPathBuffer.lineTo(
                            arcEndPointX,
                            arcEndPointY
                        )
                    } else {
                        mPathBuffer.lineTo(
                            center.x,
                            center.y
                        )
                    }
                }
            }
            mPathBuffer.close()
            mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint)
            angle += sliceAngle * phaseX
        }

        MPPointF.recycleInstance(center)
    }
}