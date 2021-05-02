package com.example.ledgr.ui.budget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar

class BudgetProgressBar: ProgressBar {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        val progressDrawable: Drawable = progressDrawable
        progressDrawable.colorFilter = translateValueToColor(progress, this.max)
        setProgressDrawable(progressDrawable)
    }

    private fun translateValueToColor(value: Int, max: Int): PorterDuffColorFilter {

        return if (value / max.toDouble() > .75 && value < max ) {
            val color = android.graphics.Color.argb(255, 255, 255, 0)
            PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
        else if (value > max) {
            val color = android.graphics.Color.argb(255, 255, 0, 0)
            PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
        else if (value == max) {
            val color = android.graphics.Color.argb(255, 0, 0, 255)
            PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
        else {
            val color = android.graphics.Color.argb(255, 56, 193, 114)
            PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }

    }

}