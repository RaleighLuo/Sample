package com.gkzxhn.supercircle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import java.util.ArrayList

/**
 * Description : 自定义圆形仪表盘View，适合根据数值显示不同等级范围的场景  <br></br>
 * author : WangGanxin <br></br>
 * date : 2017/3/23 <br></br>
 * email : mail@wangganxin.me <br></br>
 */
class CircleRangeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mPadding: Int = 0
    private var mRadius: Int = 0 // 画布边缘半径（去除padding后的半径）
    private var mStartAngle = 150 // 起始角度
    private val mSweepAngle = 240 // 绘制角度

    private val mSparkleWidth: Int // 指示标宽度
    private val mCalibrationWidth: Int // 刻度圆弧宽度
    private var mLength1: Float = 0.toFloat() // 刻度顶部相对边缘的长度
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat() // 圆心坐标

    private val mPaint: Paint
    private val mRectFProgressArc: RectF
    private val mRectFCalibrationFArc: RectF
    private val mRectText: Rect

    private val rangeColorArray: Array<CharSequence>?

    private val rangeValueArray: Array<CharSequence>?
    private val rangeTextArray: Array<CharSequence>?
    private var borderColor = ContextCompat.getColor(getContext(), R.color.wdiget_circlerange_border_color)

    private var cursorColor = ContextCompat.getColor(getContext(), R.color.wdiget_circlerange_cursor_color)
    private var extraTextColor = ContextCompat.getColor(getContext(), R.color.widget_circlerange_extra_color)
    private var rangeTextSize = sp2px(34) //中间文本大小

    private var extraTextSize = sp2px(14) //附加信息文本大小
    private val borderSize = dp2px(5) //进度圆弧宽度

    private val mBackgroundColor: Int
    private var mSection = 0 // 等分份数
    private var currentValue: String? = null
    private var extraList: List<String>? = ArrayList()

    private var isAnimFinish = true
    private var mAngleWhenAnim: Float = 0.toFloat()

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRangeView)

        rangeColorArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeColorArray)
        rangeValueArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeValueArray)
        rangeTextArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeTextArray)

        borderColor = typedArray.getColor(R.styleable.CircleRangeView_borderColor, borderColor)
        cursorColor = typedArray.getColor(R.styleable.CircleRangeView_cursorColor, cursorColor)
        extraTextColor = typedArray.getColor(R.styleable.CircleRangeView_extraTextColor, extraTextColor)

        rangeTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleRangeView_rangeTextSize, rangeTextSize)
        extraTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleRangeView_extraTextSize, extraTextSize)

        typedArray.recycle()

        if (rangeColorArray == null || rangeValueArray == null || rangeTextArray == null) {
            throw IllegalArgumentException("CircleRangeView : rangeColorArray、 rangeValueArray、rangeTextArray  must be not null ")
        }
        if (rangeColorArray.size != rangeValueArray.size
                || rangeColorArray.size != rangeTextArray.size
                || rangeValueArray.size != rangeTextArray.size) {
            throw IllegalArgumentException("arrays must be equal length")
        }

        this.mSection = rangeColorArray.size

        mSparkleWidth = dp2px(15)
        mCalibrationWidth = dp2px(10)

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND

        mRectFProgressArc = RectF()
        mRectFCalibrationFArc = RectF()
        mRectText = Rect()

        mBackgroundColor = android.R.color.transparent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mPadding = Math.max(
                Math.max(paddingLeft, paddingTop),
                Math.max(paddingRight, paddingBottom)
        )
        setPadding(mPadding, mPadding, mPadding, mPadding)

        mLength1 = mPadding.toFloat() + mSparkleWidth / 2f + dp2px(12).toFloat()

        val width = View.resolveSize(dp2px(220), widthMeasureSpec)
        mRadius = (width - mPadding * 2) / 2

        setMeasuredDimension(width, width - dp2px(30))

        mCenterY = measuredWidth / 2f
        mCenterX = mCenterY

        mRectFProgressArc.set(
                mPadding + mSparkleWidth / 2f,
                mPadding + mSparkleWidth / 2f,
                measuredWidth.toFloat() - mPadding.toFloat() - mSparkleWidth / 2f,
                measuredWidth.toFloat() - mPadding.toFloat() - mSparkleWidth / 2f
        )
        mRectFCalibrationFArc.set(
                mLength1 + mCalibrationWidth / 2f,
                mLength1 + mCalibrationWidth / 2f,
                measuredWidth.toFloat() - mLength1 - mCalibrationWidth / 2f,
                measuredWidth.toFloat() - mLength1 - mCalibrationWidth / 2f
        )

        mPaint.textSize = sp2px(10).toFloat()
        mPaint.getTextBounds("0", 0, "0".length, mRectText)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.drawColor(ContextCompat.getColor(context, mBackgroundColor))

        /**
         * 画圆弧背景
         */
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = borderSize.toFloat()
        mPaint.alpha = 80
        mPaint.color = borderColor
        canvas.drawArc(mRectFProgressArc, (mStartAngle + 1).toFloat(), (mSweepAngle - 2).toFloat(), false, mPaint)

        mPaint.alpha = 255

        /**
         * 画指示标
         */
        if (isAnimFinish) {

            val point = getCoordinatePoint(mRadius - mSparkleWidth / 2f, mStartAngle + calculateAngleWithValue(currentValue))
            mPaint.color = cursorColor
            mPaint.style = Paint.Style.FILL
            canvas.drawCircle(point[0], point[1], mSparkleWidth / 2f, mPaint)

        } else {

            val point = getCoordinatePoint(mRadius - mSparkleWidth / 2f, mAngleWhenAnim)
            mPaint.color = cursorColor
            mPaint.style = Paint.Style.FILL
            canvas.drawCircle(point[0], point[1], mSparkleWidth / 2f, mPaint)
        }

        /**
         * 画等级圆弧
         */
        mPaint.shader = null
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.alpha = 255
        mPaint.strokeCap = Paint.Cap.SQUARE
        mPaint.strokeWidth = mCalibrationWidth.toFloat()

        if (rangeColorArray != null) {
            for (i in rangeColorArray.indices) {
                mPaint.color = Color.parseColor(rangeColorArray[i].toString())
                val mSpaces = (mSweepAngle / mSection).toFloat()
                if (i == 0) {
                    canvas.drawArc(mRectFCalibrationFArc, (mStartAngle + 3).toFloat(), mSpaces, false, mPaint)
                } else if (i == rangeColorArray.size - 1) {
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle + mSpaces * i, mSpaces, false, mPaint)
                } else {
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle.toFloat() + mSpaces * i + 3f, mSpaces, false, mPaint)
                }
            }
        }

        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.FILL
        mPaint.shader = null
        mPaint.alpha = 255

        /**
         * 画等级对应值的文本（居中显示）
         */
        if (rangeColorArray != null && rangeValueArray != null && rangeTextArray != null) {

            if (!TextUtils.isEmpty(currentValue)) {
                var pos = 0

                for (i in rangeValueArray.indices) {
                    if (rangeValueArray[i] == currentValue) {
                        pos = i
                        break
                    }
                }

                mPaint.color = Color.parseColor(rangeColorArray[pos].toString())
                mPaint.textAlign = Paint.Align.CENTER

                val txt = rangeTextArray[pos].toString()

                if (txt.length <= 4) {
                    mPaint.textSize = rangeTextSize.toFloat()
                    canvas.drawText(txt, mCenterX, mCenterY + dp2px(10), mPaint)
                } else {
                    mPaint.textSize = (rangeTextSize - 10).toFloat()
                    val top = txt.substring(0, 4)
                    val bottom = txt.substring(4, txt.length)

                    canvas.drawText(top, mCenterX, mCenterY, mPaint)
                    canvas.drawText(bottom, mCenterX, mCenterY + dp2px(30), mPaint)
                }
            }
        }

        /**
         * 画附加信息
         */
        if (extraList != null && extraList!!.size > 0) {
            mPaint.alpha = 160
            mPaint.color = extraTextColor
            mPaint.textSize = extraTextSize.toFloat()
            for (i in extraList!!.indices) {
                canvas.drawText(extraList!![i], mCenterX, mCenterY + dp2px(50).toFloat() + (i * dp2px(20)).toFloat(), mPaint)
            }
        }

    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    /**
     * 根据角度和半径进行三角函数计算坐标
     * @param radius
     * @param angle
     * @return
     */
    private fun getCoordinatePoint(radius: Float, angle: Float): FloatArray {
        val point = FloatArray(2)

        var arcAngle = Math.toRadians(angle.toDouble()) //将角度转换为弧度
        if (angle < 90) {
            point[0] = (mCenterX + Math.cos(arcAngle) * radius).toFloat()
            point[1] = (mCenterY + Math.sin(arcAngle) * radius).toFloat()
        } else if (angle == 90f) {
            point[0] = mCenterX
            point[1] = mCenterY + radius
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0
            point[0] = (mCenterX - Math.cos(arcAngle) * radius).toFloat()
            point[1] = (mCenterY + Math.sin(arcAngle) * radius).toFloat()
        } else if (angle == 180f) {
            point[0] = mCenterX - radius
            point[1] = mCenterY
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0
            point[0] = (mCenterX - Math.cos(arcAngle) * radius).toFloat()
            point[1] = (mCenterY - Math.sin(arcAngle) * radius).toFloat()
        } else if (angle == 270f) {
            point[0] = mCenterX
            point[1] = mCenterY - radius
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0
            point[0] = (mCenterX + Math.cos(arcAngle) * radius).toFloat()
            point[1] = (mCenterY - Math.sin(arcAngle) * radius).toFloat()
        }

        return point
    }

    /**
     * 根据起始角度计算对应值应显示的角度大小
     */
    private fun calculateAngleWithValue(level: String?): Float {

        var pos = -1

        for (j in rangeValueArray!!.indices) {
            if (rangeValueArray[j] == level) {
                pos = j
                break
            }
        }

        val degreePerSection = 1f * mSweepAngle / mSection

        return if (pos == -1) {
            0f
        } else if (pos == 0) {
            degreePerSection / 2
        } else {
            pos * degreePerSection + degreePerSection / 2
        }
    }

    /**
     * 设置值并播放动画
     *
     * @param value  值
     * @param extras 底部附加信息
     */
    @JvmOverloads
    fun setValueWithAnim(value: String, extras: List<String>? = null) {
        if (!isAnimFinish) {
            return
        }

        this.currentValue = value
        this.extraList = extras

        // 计算最终值对应的角度，以扫过的角度的线性变化来播放动画
        val degree = calculateAngleWithValue(value)

        val degreeValueAnimator = ValueAnimator.ofFloat(mStartAngle.toFloat(), mStartAngle + degree)
        degreeValueAnimator.addUpdateListener { animation ->
            mAngleWhenAnim = animation.animatedValue as Float
            postInvalidate()
        }

        val delay: Long = 1500

        val animatorSet = AnimatorSet()
        animatorSet
                .setDuration(delay)
                .playTogether(degreeValueAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                isAnimFinish = false
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isAnimFinish = true
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                isAnimFinish = true
            }
        })
        animatorSet.start()
    }
}
/**
 * 设置值并播放动画
 *
 * @param value 值
 */
