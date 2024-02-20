package com.sk1982.purduemap.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import com.google.android.libraries.maps.model.BitmapDescriptor
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.sk1982.purduemap.App

object BitmapUtils {
    fun createPureTextIcon(text: String, sizeDp: Float = 10f): BitmapDescriptor {
        val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeDp, App.context!!.resources.displayMetrics)

        val strokePaint = Paint().apply {
            isAntiAlias = true
            textSize = size
            style = Paint.Style.STROKE
            strokeWidth = size / 3f
            color = Color.WHITE
        }

        val textPaint = Paint().apply {
            isAntiAlias = true
            textSize = size
        }
        val textWidth = textPaint.measureText(text)
        val textHeight = textPaint.textSize

        val strokeWidth = textWidth + strokePaint.strokeWidth
        val strokeHeight = textHeight + strokePaint.strokeWidth

        val width = strokeWidth.toInt()
        val height = strokeHeight.toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.translate(0f, textHeight)

        canvas.drawText(text, 0f, 0f, strokePaint)
        canvas.drawText(text, 0f, 0f, textPaint)
        return BitmapDescriptorFactory.fromBitmap(image)
    }
}