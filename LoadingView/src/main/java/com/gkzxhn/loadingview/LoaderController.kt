package com.gkzxhn.loadingview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.view.animation.LinearInterpolator

/*
 * Copyright 2016 Elye Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

internal class LoaderController(private val loaderView: LoaderView) : ValueAnimator.AnimatorUpdateListener {
    private lateinit var rectPaint: Paint
    private var linearGradient: LinearGradient? = null
    private var progress: Float = 0.toFloat()
    private var valueAnimator: ValueAnimator? = null
    private var widthWeight = LoaderConstant.MAX_WEIGHT
    private var heightWeight = LoaderConstant.MAX_WEIGHT
    private var useGradient = LoaderConstant.USE_GRADIENT_DEFAULT
    private var corners = LoaderConstant.CORNER_DEFAULT
    private val MAX_COLOR_CONSTANT_VALUE = 255
    private val ANIMATION_CYCLE_DURATION = 750 //milis
    init {
        init()
    }
    private fun init(){
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        loaderView.setRectColor(rectPaint)
        setValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
    }

    @JvmOverloads
    fun onDraw(canvas: Canvas, left_pad: Float = 0f, top_pad: Float = 0f, right_pad: Float = 0f, bottom_pad: Float = 0f) {
        val margin_height = canvas.height * (1 - heightWeight) / 2
        rectPaint.alpha = (progress * MAX_COLOR_CONSTANT_VALUE).toInt()
        if (useGradient) {
            prepareGradient(canvas.width * widthWeight)
        }
        canvas.drawRoundRect(RectF(0 + left_pad,
                margin_height + top_pad,
                canvas.width * widthWeight - right_pad,
                canvas.height.toFloat() - margin_height - bottom_pad),
                corners.toFloat(), corners.toFloat(),
                rectPaint)
    }

    fun onSizeChanged() {
        linearGradient = null
        startLoading()
    }

    private fun prepareGradient(width: Float) {
        if (linearGradient == null) {
            linearGradient = LinearGradient(0f, 0f, width, 0f, rectPaint.color,
                    LoaderConstant.COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR)
        }
        rectPaint.shader = linearGradient
    }

    fun startLoading() {
        if (valueAnimator != null && !loaderView.valueSet()) {
            valueAnimator?.cancel()
            init()
            valueAnimator?.start()
        }
    }

    fun setHeightWeight(heightWeight: Float) {
        this.heightWeight = validateWeight(heightWeight)
    }

    fun setWidthWeight(widthWeight: Float) {
        this.widthWeight = validateWeight(widthWeight)
    }

    fun setUseGradient(useGradient: Boolean) {
        this.useGradient = useGradient
    }

    fun setCorners(corners: Int) {
        this.corners = corners
    }

    private fun validateWeight(weight: Float): Float {
        if (weight > LoaderConstant.MAX_WEIGHT)
            return LoaderConstant.MAX_WEIGHT
        return if (weight < LoaderConstant.MIN_WEIGHT) LoaderConstant.MIN_WEIGHT else weight
    }

    fun stopLoading() {
        valueAnimator?.cancel()
        setValueAnimator(progress, 0f, 0)
        valueAnimator?.start()
    }

    private fun setValueAnimator(begin: Float, end: Float, repeatCount: Int) {
        valueAnimator = ValueAnimator.ofFloat(begin, end)
        valueAnimator?.repeatCount = repeatCount
        valueAnimator?.duration = ANIMATION_CYCLE_DURATION.toLong()
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener(this)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        progress = valueAnimator.animatedValue as Float
        loaderView.invalidate()
    }

    fun removeAnimatorUpdateListener() {
        valueAnimator?.removeUpdateListener(this)
        valueAnimator?.cancel()
    }


}
