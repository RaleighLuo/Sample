package com.gkzxhn.supercircle

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.super_circle_layout.*
import java.util.*

class SuperCircleActivity : AppCompatActivity() {
    private lateinit var valueArray: Array<String>
    private lateinit var random: Random
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.super_circle_layout)
        mSuperCircleView.setShowSelect(false)
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.setTarget(textView)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener { animation ->
            val i = Integer.valueOf(animation.animatedValue.toString())
            textView.text = i.toString() + ""
            mSuperCircleView.setSelect((360 * (i / 100f)).toInt())
        }
        valueAnimator.start()
        /*****************************************************************/
        valueArray = resources.getStringArray(R.array.circlerangeview_values)
        random = Random()
        mCircleRangeView.setOnClickListener {
            val extras = ArrayList<String>()
            extras.add("收缩压：116")
            extras.add("舒张压：85")
            val i = random.nextInt(valueArray.size)
            mCircleRangeView.setValueWithAnim(valueArray[i], extras)

        }
    }
}
