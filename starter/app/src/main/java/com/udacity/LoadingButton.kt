package com.udacity

import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                valueAnimator= ValueAnimator.ofFloat(0f, 1f).apply {
                    setBackgroundColor(Color.parseColor("#004349"))
                    addUpdateListener {
                        val animatedValue = animatedValue as Float
                        Log.d(TAG, "progress:$animatedValue")
                        invalidate()
                    }
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    duration = 3000
                    start()
                }
                disableLoadingButton()
            }

            ButtonState.Completed -> {
                setBackgroundColor(Color.parseColor("#07C2AA"))
                valueAnimator.cancel()
                invalidate()
                enableLoadingButton()
            }
        }
        invalidate()

    }

    private fun enableLoadingButton() {
        custom_button.isEnabled=true
    }

    private fun disableLoadingButton() {
        custom_button.isEnabled=false
    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

    init {

        setBackgroundColor(Color.parseColor("#07C2AA"))

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}