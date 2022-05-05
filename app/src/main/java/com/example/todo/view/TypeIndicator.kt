package com.example.todo.view

import android.graphics.*
import android.graphics.drawable.Drawable

class TypeIndicator(color: Int) : Drawable() {
    private val radius: Float = 12f
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = color
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(RectF(bounds), radius, radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}