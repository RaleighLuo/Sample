package com.gkzxhn.slantedtextview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.slanted_text_view_layout.*
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_main.*


class SlantedTextViewActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slanted_text_view_layout)
        setSupportActionBar(mToolbar);
        fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        })
        mSlantedTextView.setText("PHP")
                .setTextColor(Color.WHITE)
                .setSlantedBackgroundColor(Color.BLACK)
                .setTextSize(18)
                .setSlantedLength(50)
                .setMode(SlantedTextView.MODE_LEFT_BOTTOM)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()
        //noinspection SimplifiableIfStatement
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}