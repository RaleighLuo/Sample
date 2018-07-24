package com.gkzxhn.loadingview

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.loading_view_layout.*

class LoadingViewActivity : AppCompatActivity() {

    private val WAIT_DURATION = 5000
    private var dummyWait: DummyWait? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_view_layout)
        loadData()
    }

    private fun loadData() {
        dummyWait?.cancel(true)
        dummyWait = DummyWait()
        dummyWait?.execute()
    }

    private fun postLoadData() {
        Handler().post {
            txt_name.text = "Mr. Donald Trump"
            txt_title.text = "President of United State (2017 - now)"
            txt_phone.text = "+001 2345 6789"
            txt_email.text = "donald.trump@donaldtrump.com"
            image_icon.setImageResource(R.drawable.trump)
        }
    }

    fun resetLoader(view: View) {
        txt_name.resetLoader()
        txt_title.resetLoader()
        txt_phone.resetLoader()
        txt_email.resetLoader()
        image_icon.resetLoader()
        loadData()
    }

     inner class DummyWait : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                Thread.sleep(WAIT_DURATION.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            postLoadData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dummyWait?.cancel(true)
    }
}
