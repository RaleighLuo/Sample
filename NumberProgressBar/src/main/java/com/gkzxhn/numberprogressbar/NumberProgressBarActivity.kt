package com.gkzxhn.numberprogressbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.number_progress_bar_layout.*


import java.util.Timer
import java.util.TimerTask


class NumberProgressBarActivity : AppCompatActivity(), NumberProgressBar.OnProgressBarListener {
    private lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_progress_bar_layout)
        bnp.setOnProgressBarListener(this)
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread { bnp.incrementProgressBy(1) }
            }
        }, 1000, 100)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onProgressChange(current: Int, max: Int) {
        if (current == max) {
            Toast.makeText(applicationContext, getString(R.string.finish), Toast.LENGTH_SHORT).show()
            bnp.mCurrentProgress = 0
        }
    }
}
