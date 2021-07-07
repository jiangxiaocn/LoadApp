package com.udacity

import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonBgColor = R.attr.buttonBackgroundColor
    private var buttonText: String
    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                    this@LoadingButton.buttonText = ("Downloading")
                    buttonBgColor = Color.parseColor("#004349")
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
                this@LoadingButton.buttonText = ("Downloaded")
                buttonBgColor = Color.parseColor("#07C2AA")
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
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LoadingButton,
                0, 0).apply {

            try {
                buttonText = getString(R.styleable.LoadingButton_buttonText).toString()
                buttonBgColor = getColor(R.styleable.LoadingButton_buttonBackgroundColor,Color.parseColor("#07C2AA"))
            } finally {
                recycle()
            }
        }

    }

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
    }

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#07C2AA")
    }

    private val downloadingBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#004349")
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(buttonBgColor)
        canvas?.drawRect(0f, 0f, 100f, 100f, bgPaint)

        if (buttonState == ButtonState.Loading) {
            canvas?.drawRect(0f, 0f, 100f, 100f, downloadingBgPaint)
        }

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