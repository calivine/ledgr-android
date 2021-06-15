package com.example.ledgr.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import com.example.ledgr.R

/**
 * TODO: document your custom view class.
 */
class PieChart : View {

    private var _title: String? = resources.getString(R.string.app_name)
    private var _textColor: Int = getColor(resources, R.color.black, null)
    private var _dimension: Float = 0f // TODO: use a default from R.dimen...
    private var _boxColor: Int = Color.WHITE


    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private val _data: List<Any> = listOf(0)
    val data: List<Any> get() = _data





    private val boxPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL

    }

    private val boxOutline = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = textColor
        if (textHeight == 0f) {
            textHeight = textSize
        } else {
            textSize = textHeight
        }
    }

    private val piePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = textHeight
    }

    private val shadowPaint = Paint(0).apply {
        color = 0x101010
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }


    /**
     * The text to draw
     */
    var title: String?
        get() = _title
        set(value) {
            _title = value
            invalidateTextPaintAndMeasurements()
        }


    /**
     * The font color
     */
    var textColor: Int
        get() = _textColor
        set(value) {
            _textColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var dimension: Float
        get() = _dimension
        set(value) {
            _dimension = value
            invalidateTextPaintAndMeasurements()
        }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PieChart, defStyle, 0
        )

        _title = a.getString(
            R.styleable.PieChart_title
        )
        _textColor = a.getColor(
            R.styleable.PieChart_titleColor,
            textColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _dimension = a.getDimension(
            R.styleable.PieChart_dimension,
            dimension
        )


        a.recycle()

        // Set up a default TextPaint object


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = dimension
            it.color = textColor
            textWidth = it.measureText(title)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        val minh: Int = View.MeasureSpec.getSize(w) - textWidth.toInt() + paddingBottom + paddingTop
        val h: Int = View.resolveSizeAndState(
            View.MeasureSpec.getSize(w) - textWidth.toInt(),
            heightMeasureSpec,
            0
        )

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Account for padding
        var xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        // Account for the label
        xpad += textWidth

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        // Figure out how big we can make the pie.
        val diameter = Math.min(ww, hh)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
        canvas.apply {
            // Draw the shadow
            drawOval(shadowBounds, shadowPaint)


            // Draw the label text
            drawText(data[mCurrentItem].mLabel, textX, textY, textPaint)

            // Draw the pie slices
            data.forEach {
                piePaint.shader = it.mShader
                drawArc(bounds,
                    360 - it.endAngle,
                    it.endAngle - it.startAngle,
                    true, piePaint)
            }

            // Draw the pointer
            drawLine(textX, pointerY, pointerX, pointerY, textPaint)
            drawCircle(pointerX, pointerY, pointerSize, mTextPaint)
        }
        */
    }


}