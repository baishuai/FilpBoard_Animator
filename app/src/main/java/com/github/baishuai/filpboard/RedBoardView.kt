package com.github.baishuai.filpboard

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Android 动画练习
 */
class RedBoardView : View {


    private lateinit var mImage: Drawable

    private var mDrawableWidth: Int = 0
    private var mDrawableHeight: Int = 0

    private var rotateR: Float = 0f
    private var degreeY: Float = 0f
    private var degreeL: Float = 0f

    private var pLeft = paddingLeft
    private var pTop = paddingTop
    private var pRight = paddingRight
    private var pBottom = paddingBottom

    private val camera = Camera()

    private val degreeAnimator = ObjectAnimator.ofFloat(this, "degreeY", 0f, -40f)
    private val rotateAnimator = ObjectAnimator.ofFloat(this, "rotateR", 0f, -270f)
    private val lastAnimator = ObjectAnimator.ofFloat(this, "degreeL", 0f, -30f)

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun setDegreeY(y: Float) {
        degreeY = y
        invalidate()
    }

    fun setDegreeL(y: Float) {
        degreeL = y
        invalidate()
    }

    fun setRotateR(r: Float) {
        rotateR = r
        invalidate()
    }

    fun startFlip() {
        val set = AnimatorSet()
        degreeAnimator.duration = 1000
        rotateAnimator.duration = 1000
        lastAnimator.duration = 1000
        set.playSequentially(degreeAnimator, rotateAnimator, lastAnimator)
        set.start()
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.RedBoardView, defStyle, 0)

        var d: Drawable? = null
        if (a.hasValue(R.styleable.RedBoardView_src)) {
            d = a.getDrawable(R.styleable.RedBoardView_src)
        }
        if (d == null) {
            d = ContextCompat.getDrawable(context, R.drawable.google_map)
        }
        updateDrawable(d!!)
        a.recycle()
    }

    fun setImageDrawable(drawable: Drawable) {
        val oldWidth = mDrawableWidth;
        val oldHeight = mDrawableHeight;
        updateDrawable(drawable)
        if (oldWidth != mDrawableWidth || oldHeight != mDrawableHeight) {
            requestLayout()
        }
        invalidate()
    }

    private fun updateDrawable(d: Drawable) {
        mDrawableWidth = d.intrinsicWidth
        mDrawableHeight = d.intrinsicHeight
        d.setBounds(0, 0, mDrawableWidth, mDrawableHeight)
        mImage = d
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension((mDrawableWidth * 1.6).toInt(), (mDrawableHeight * 1.6).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - pLeft - pRight
        val contentHeight = width - pTop - pBottom
        val centerX = contentWidth / 2
        val centerY = contentHeight / 2
        val x = centerX - mDrawableWidth / 2
        val y = centerY - mDrawableHeight / 2
        mImage.setBounds(x, y, x + mDrawableWidth, y + mDrawableHeight)

        canvas.save()
        camera.save()
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        canvas.rotate(rotateR)
        canvas.clipRect(-centerX, -centerY, 0, centerY)
        camera.rotateY(-degreeL)
        camera.applyToCanvas(canvas)
        canvas.rotate(-rotateR)
        canvas.translate(-centerX.toFloat(), -centerY.toFloat())
        mImage.draw(canvas)
        camera.restore()
        canvas.restore()

        canvas.save()
        camera.save()
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        canvas.rotate(rotateR)
        canvas.clipRect(0, -centerY, centerX, centerY)
        camera.rotateY(degreeY)
        camera.applyToCanvas(canvas)
        canvas.rotate(-rotateR)
        canvas.translate(-centerX.toFloat(), -centerY.toFloat())
        mImage.draw(canvas)
        camera.restore()
        canvas.restore()
    }
}
