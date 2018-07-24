package com.gkzxhn.rollingtextview

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import java.util.*
import java.util.Arrays.asList
import android.annotation.SuppressLint
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateDecelerateInterpolator
import com.gkzxhn.rollingtextview.strategy.Direction
import com.gkzxhn.rollingtextview.strategy.Strategy
import kotlinx.android.synthetic.main.rolling_text_view_layout.*
import java.text.SimpleDateFormat


class RollingTextViewActivity: AppCompatActivity() {
    private val handler = Handler()

    private val list = Arrays.asList("1", "9", "12", "19", "24", "36", "47",
            "56", "63", "78", "89", "95", "132", "289", "312", "400")
    private var idx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rolling_text_view_layout)
        val normal = findViewById<RollingTextView>(R.id.rollingTextView)
        normal.addCharOrder(CharOrder.Number)
        normal.animationDuration = 2000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                normal.setText(list[idx % list.size])
                handler.postDelayed(this, 3000L)
            }
        }, 2000L)


        val sameDirection = findViewById<RollingTextView>(R.id.rollingTextView2)
        sameDirection.addCharOrder(CharOrder.Number)
        sameDirection.animationDuration = 2000L
        sameDirection.charStrategy = Strategy.SameDirectionAnimation(Direction.SCROLL_DOWN)
        handler.postDelayed(object : Runnable {
            override fun run() {
                sameDirection.setText(list[idx % list.size])
                handler.postDelayed(this, 3000L)
            }
        }, 2000L)

        val carryBit = findViewById<RollingTextView>(R.id.rollingTextView3)
        carryBit.addCharOrder(CharOrder.Number)
        carryBit.animationDuration = 2000L
        carryBit.charStrategy = Strategy.CarryBitAnimation()
        handler.postDelayed(object : Runnable {
            override fun run() {
                carryBit.setText(list[idx++ % list.size])
                handler.postDelayed(this, 3000L)
            }
        }, 2000L)

        val stickyText = findViewById<RollingTextView>(R.id.stickyText)
        stickyText.animationDuration = 3000L
        stickyText.addCharOrder("0123456789abcdef")
        stickyText.charStrategy = Strategy.StickyAnimation(0.9)
        handler.postDelayed({ stickyText.setText("eeee") }, 2000L)

        val stickyText2 = findViewById<RollingTextView>(R.id.stickyText2)
        stickyText2.animationDuration = 3000L
        stickyText2.addCharOrder("0123456789abcdef")
        stickyText2.charStrategy = Strategy.StickyAnimation(0.2)
        handler.postDelayed({ stickyText2.setText("eeee") }, 2000L)


        val alphaBetView = findViewById<RollingTextView>(R.id.alphaBetView)
        alphaBetView.animationDuration = 2000L
        alphaBetView.charStrategy = Strategy.NormalAnimation()
        alphaBetView.addCharOrder(CharOrder.Alphabet)
        alphaBetView.addCharOrder(CharOrder.UpperAlphabet)
        alphaBetView.addCharOrder(CharOrder.Number)
        alphaBetView.addCharOrder(CharOrder.Hex)
        alphaBetView.addCharOrder(CharOrder.Binary)
        alphaBetView.animationInterpolator = AccelerateDecelerateInterpolator()
        alphaBetView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //finsih
            }
        })
        alphaBetView.setText("i am a text")


        val timeView = findViewById<RollingTextView>(R.id.timeView)
        timeView.animationDuration = 300
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("HH:mm:ss")
        handler.post(object : Runnable {
            override fun run() {
                timeView.setText(format.format(Date()))
                handler.postDelayed(this, 1000L)
            }
        })

        val carryView = findViewById<RollingTextView>(R.id.carryTextView)
        carryView.animationDuration = 13000L
        carryView.addCharOrder(CharOrder.Number)
        carryView.charStrategy = Strategy.CarryBitAnimation()
        carryView.setText("0")
        carryView.setText("1290")

        val charOrder1 = findViewById<RollingTextView>(R.id.charOrderExample1)
        charOrder1.animationDuration = 4000L
        charOrder1.addCharOrder("abcdefg")
        charOrder1.setText("a")

        val charOrder2 = findViewById<RollingTextView>(R.id.charOrderExample2)
        charOrder2.animationDuration = 4000L
        charOrder2.addCharOrder("adg")
        charOrder2.setText("a")

        handler.postDelayed({
            charOrder1.setText("g") //move from a to g

            charOrder2.setText("g") //just like charOrder1 but with differen charOder
        }, 2000L)
    }

}