package com.gkzxhn.materiallogin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.webkit.WebView
import cn.fanrunqi.materiallogin.R
import kotlinx.android.synthetic.main.register_layout.register_layout_et_username
as etUsername
import kotlinx.android.synthetic.main.register_layout.register_layout_et_password
as etPassword
import kotlinx.android.synthetic.main.register_layout.register_layout_et_repeatpassword
as etRepetpassword
import kotlinx.android.synthetic.main.register_layout.register_layout_btn_go
as btnGo
import kotlinx.android.synthetic.main.register_layout.register_layout_cv_reigster
as mCardView
import kotlinx.android.synthetic.main.register_layout.register_layout_fab_close
as fabClose

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        ShowEnterAnimation()
    }
    fun onClick(view: View){
        animateRevealClose()
    }

    override fun onBackPressed() {
        animateRevealClose()
    }

    private fun ShowEnterAnimation() {
        //代码中设置转场动画，或theme中设置<item name="android:sharedElementEnterTransition">@stransition/xx</item>
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
//        window.enterTransition
//        window.exitTransition
        //共享元素变换动画,决定了共享view元素是如何变换的
        window.sharedElementEnterTransition = transition

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                //隐藏内容
                mCardView.setVisibility(View.GONE)
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                //共享元素移动到中间位置后，开半圆揭露显示动画
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
    }

    fun animateRevealShow() {
        //揭露动画，为用户提供视觉连续性。  控件圆形显示
        val mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, mCardView.getWidth() / 2, 0, (fabClose.getWidth() / 2).toFloat(), mCardView.getHeight().toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }

            override fun onAnimationStart(animation: Animator) {
                mCardView.setVisibility(View.VISIBLE)
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }


    fun animateRevealClose() {
        //揭露动画，为用户提供视觉连续性。  控件圆形隐藏
        val mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, mCardView.getWidth() / 2, 0, mCardView.getHeight().toFloat(), (fabClose.getWidth() / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCardView.setVisibility(View.INVISIBLE)
                super.onAnimationEnd(animation)
                fabClose.setImageResource(R.drawable.plus)
                super@RegisterActivity.onBackPressed()
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }
}