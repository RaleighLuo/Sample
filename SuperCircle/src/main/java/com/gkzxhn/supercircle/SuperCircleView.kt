package com.gkzxhn.supercircle

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by gjy on 16/7/4.
 */

class SuperCircleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val TAG = "SuperCircleView"

    private var mViewWidth: Int = 0 //view的宽
    private var mViewHeight: Int = 0    //view的高
    private var mViewCenterX: Int = 0   //view宽的中心点
    private var mViewCenterY: Int = 0   //view高的中心点
    private val mMinRadio: Int //最里面白色圆的半径
    private val mRingWidth: Float //圆环的宽度
    private var mSelect: Int = 0    //分成多少段
    private val mSelectAngle: Int   //每个圆环之间的间隔
    private val mMinCircleColor: Int    //最里面圆的颜色
    private val mMaxCircleColor: Int    //最外面圆的颜色
    private val mRingNormalColor: Int    //默认圆环的颜色
    private val mPaint: Paint
    private var color = IntArray(3)   //渐变颜色

    private var mRingAngleWidth: Float = 0.toFloat()  //每一段的角度

    private var mRectF: RectF? = null //圆环的矩形区域
    private var mSelectRing = 0        //要显示几段彩色

    private var isShowSelect = false   //是否显示断

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SuperCircleView)
        mMinRadio = a.getInteger(R.styleable.SuperCircleView_min_circle_radio, 400)

        mRingWidth = a.getFloat(R.styleable.SuperCircleView_ring_width, 40f)

        mSelect = a.getInteger(R.styleable.SuperCircleView_select, 7)
        mSelectAngle = a.getInteger(R.styleable.SuperCircleView_selec_angle, 3)

        mMinCircleColor = a.getColor(R.styleable.SuperCircleView_circle_color, context.resources.getColor(R.color.white))
        mMaxCircleColor = a.getColor(R.styleable.SuperCircleView_max_circle_color, context.resources.getColor(R.color.huise2))
        mRingNormalColor = a.getColor(R.styleable.SuperCircleView_ring_normal_color, context.resources.getColor(R.color.huise))

        isShowSelect = a.getBoolean(R.styleable.SuperCircleView_is_show_select, false)
        mSelectRing = a.getInt(R.styleable.SuperCircleView_ring_color_select, 0)
        a.recycle()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.isAntiAlias = true
        this.setWillNotDraw(false)
        color[0] = Color.parseColor("#8EE484")
        color[1] = Color.parseColor("#97C0EF")
        color[2] = Color.parseColor("#8EE484")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mViewWidth = measuredWidth
        mViewHeight = measuredHeight
        mViewCenterX = mViewWidth / 2
        mViewCenterY = mViewHeight / 2
        mRectF = RectF(mViewCenterX.toFloat() - mMinRadio.toFloat() - mRingWidth / 2, mViewCenterY.toFloat() - mMinRadio.toFloat() - mRingWidth / 2, mViewCenterX.toFloat() + mMinRadio.toFloat() + mRingWidth / 2, mViewCenterY.toFloat() + mMinRadio.toFloat() + mRingWidth / 2)
        mRingAngleWidth = ((360 - mSelect * mSelectAngle) / mSelect).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 显示彩色断大于总共的段数是错误的
         */
        if (isShowSelect && mSelectRing > mSelect) {
            return
        }
        mPaint.color = mMaxCircleColor
        canvas.drawCircle(mViewCenterX.toFloat(), mViewCenterY.toFloat(), mMinRadio.toFloat() + mRingWidth + 20f, mPaint)
        mPaint.color = mMinCircleColor
        canvas.drawCircle(mViewCenterX.toFloat(), mViewCenterY.toFloat(), mMinRadio.toFloat(), mPaint)
        //画默认圆环
        drawNormalRing(canvas)
        //画彩色圆环
        drawColorRing(canvas)


    }

    /**
     * 画彩色圆环
     *
     * @param canvas
     */
    private fun drawColorRing(canvas: Canvas) {
        val ringColorPaint = Paint(mPaint)
        ringColorPaint.style = Paint.Style.STROKE
        ringColorPaint.strokeWidth = mRingWidth
        ringColorPaint.shader = SweepGradient(mViewCenterX.toFloat(), mViewCenterX.toFloat(), color, null)

        if (!isShowSelect) {
            canvas.drawArc(mRectF, 270f, mSelectRing.toFloat(), false, ringColorPaint)
            return
        }

        if (mSelect == mSelectRing && mSelectRing != 0 && mSelect != 0) {
            canvas.drawArc(mRectF, 270f, 360f, false, ringColorPaint)
        } else {
            Log.d(TAG, (mRingAngleWidth * mSelectRing + mSelectAngle.toFloat() + mSelectRing.toFloat()).toString() + "")
            canvas.drawArc(mRectF, 270f, mRingAngleWidth * mSelectRing + mSelectAngle * mSelectRing, false, ringColorPaint)
        }

        ringColorPaint.shader = null
        ringColorPaint.color = mMaxCircleColor
        for (i in 0 until mSelectRing) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + i * mSelectAngle), mSelectAngle.toFloat(), false, ringColorPaint)
        }
    }

    /**
     * 画默认圆环
     *
     * @param canvas
     */
    private fun drawNormalRing(canvas: Canvas) {
        val ringNormalPaint = Paint(mPaint)
        ringNormalPaint.style = Paint.Style.STROKE
        ringNormalPaint.strokeWidth = mRingWidth
        ringNormalPaint.color = mRingNormalColor
        canvas.drawArc(mRectF, 270f, 360f, false, ringNormalPaint)
        if (!isShowSelect) {
            return
        }
        ringNormalPaint.color = mMaxCircleColor
        for (i in 0 until mSelect) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + i * mSelectAngle), mSelectAngle.toFloat(), false, ringNormalPaint)
        }
    }

    /**
     * 显示几段
     *
     * @param i
     */
    fun setSelect(i: Int) {
        this.mSelectRing = i
        this.invalidate()
    }

    /**
     * 断的总数
     *
     * @param i
     */
    fun setSelectCount(i: Int) {
        this.mSelect = i
    }


    /**
     * 是否显示断
     *
     * @param b
     */
    fun setShowSelect(b: Boolean) {
        this.isShowSelect = b
    }


    fun setColor(color: IntArray) {
        this.color = color
    }
}
