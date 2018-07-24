package com.gkzxhn.ringprogressbar


import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.ring_progress_bar_layout.*

class RingProgressBarActivity : AppCompatActivity() {

    private var mProgress = 0

    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {

            if (msg.what == 0) {
                if (mProgress < 100) {
                    mProgress++
                    mRingProgressBar1.setProgress(mProgress)
                    mRingProgressBar2.setProgress(mProgress)
                    mRingProgressBar1.setOnProgressListener(object : RingProgressBar.OnProgressListener {

                        override fun progressToComplete() {
                            // Here after the completion of the processing
                        }
                    })

                    mRingProgressBar2.setOnProgressListener(object : RingProgressBar.OnProgressListener {

                        override fun progressToComplete() {
                            // Here after the completion of the processing
                        }
                    })
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.ring_progress_bar_layout)
        Thread(Runnable {
            for (i in 0..99) {
                try {
                    Thread.sleep(100)

                    mHandler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()
    }


    override fun onDestroy() {

        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
