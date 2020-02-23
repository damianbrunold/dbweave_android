package ch.dabsoft.dbweave

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class WeaveGridView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply { setARGB(255, 0, 0, 0) }
    private val fillPaint = Paint().apply { setARGB(255, 0, 0, 0) }
    private var gw: Int = 0
    private var gh: Int = 0
    private var dx: Float = 64.0f

    var data: GridData? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Account for padding
        var xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        gw = (ww / dx).toInt()
        gh = (hh / dx).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            for (x in 0..gw) {
                canvas.drawLine(x * dx, 0.0f, x * dx, height.toFloat(), paint)
            }
            for (y in 0..gh) {
                canvas.drawLine(0.0f, y * dx, width.toFloat(), y * dx, paint)
            }

            if (data != null) {
                var rect = Rect()
                for (x in 0..gw) {
                    for (y in 0..gh) {
                        if (data!![x, y] != 0) {
                            rect.left = (x * dx).toInt() + 1
                            rect.top = (y * dx).toInt() + 1
                            rect.right = ((x + 1) * dx).toInt() - 1
                            rect.bottom = ((y + 1) * dx).toInt() - 1
                            canvas.drawRect(rect, fillPaint);
                        }
                    }
                }
            }
        }
    }

    private val myListener =  object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

    private val detector: GestureDetector = GestureDetector(context, myListener)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return detector.onTouchEvent(event).let { result ->
            if (!result) {
                if (event?.action == MotionEvent.ACTION_UP) {
                    toggleRaw(event!!.x, event!!.y)
                    true
                } else false
            } else true
        }
    }

    fun toggleRaw(x: Float, y: Float) {
        val xx = (x / dx).toInt()
        val yy = (y / dx).toInt()
        data!![xx, yy] = 1 - data!![xx, yy]
        invalidate()
    }

}