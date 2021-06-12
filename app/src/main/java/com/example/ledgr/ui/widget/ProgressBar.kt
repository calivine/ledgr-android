package com.example.ledgr.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import com.example.ledgr.R
import com.google.android.material.color.MaterialColors

class ProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val boxOutline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.ledgr_primary)
    }

    private val warningPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.warning)
    }

    private val overBudgetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.alert)
    }

    private val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = MaterialColors.getColor(context, R.attr.colorBar, Color.WHITE)
    }


    private val _maxHeight = 75F

    private var _planned = 0F
    private var _actual = 0F

    private var _amountFull = 0F

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProgressBar,
            0,0
        ).apply {
            try {
                _actual = getFloat(R.styleable.ProgressBar_actual, 0F)
                _planned = getFloat(R.styleable.ProgressBar_planned, 0F)
            } finally {
                recycle()
            }
        }
    }

    fun getActual(): Float {
        return _actual
    }

    fun setActual(actual: Float) {
        _actual = actual
        invalidate()
        requestLayout()
    }



    fun getPlanned(): Float {
        return _planned
    }

    fun setPlanned(planned: Float) {
        _planned = planned
        invalidate()
        requestLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = (width - paddingLeft - paddingRight)
        val contentHeight = (height - paddingTop - paddingBottom)

        val progress = _actual / _planned

        val scale = resources.displayMetrics.density

        val top = 0F + paddingTop
        val left = 0F
        val right = contentWidth * progress
        val rightBorder = contentWidth * 1F
        val bottom = _maxHeight * scale + paddingBottom



        canvas.apply {
            drawRect(left, top, rightBorder, bottom, boxPaint)
            drawRect(left, top, right, bottom, if(progress < .75) normalPaint else if (progress in .75..1.0) warningPaint else overBudgetPaint)
            drawRect(left, top, rightBorder, bottom, boxOutline)

        }


    }


}