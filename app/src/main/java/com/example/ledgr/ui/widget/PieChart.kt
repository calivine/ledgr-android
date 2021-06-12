package com.example.ledgr.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.ledgr.R

/**
 * TODO: document your custom view class.
 */
class PieChart : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _textColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...
    private var _boxColor: Int = Color.WHITE

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f





    private val boxPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL

    }

    private val boxOutline = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }


    /**
     * The text to draw
     */
    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
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
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

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

        _exampleString = a.getString(
            R.styleable.PieChart_exampleString
        )
        _textColor = a.getColor(
            R.styleable.PieChart_exampleColor,
            textColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.PieChart_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.PieChart_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.PieChart_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = exampleDimension
            it.color = textColor
            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = (width - paddingLeft - paddingRight)
        val contentHeight = (height/2 - paddingTop - paddingBottom)

        




        canvas.apply {


            drawRect(paddingLeft* 2F, contentHeight /5F, contentWidth.toFloat(), (contentHeight/5F) + 50, boxOutline)
        }


        exampleString?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint
            )
        }

        // Draw the example drawable on top of the text.
        exampleDrawable?.let {
            it.setBounds(
                paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight
            )
            it.draw(canvas)
        }
    }
}