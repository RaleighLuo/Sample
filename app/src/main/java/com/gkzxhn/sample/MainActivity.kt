package com.gkzxhn.sample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gkzxhn.loadingview.LoadingViewActivity
import com.gkzxhn.materiallogin.LoginActivity
import com.gkzxhn.ringprogressbar.RingProgressBarActivity
import com.gkzxhn.rollingtextview.RollingTextViewActivity
import com.gkzxhn.sample.adapter.MainAdapter
import com.gkzxhn.sample.adapter.OnItemClickListener
import com.gkzxhn.scratchview.ScratchDemoActivity
import com.gkzxhn.slantedtextview.SlantedTextViewActivity
import com.gkzxhn.supercircle.SuperCircleActivity
import com.gkzxhn.switchbutton.SwitchButtonActivity
import com.gkzxhn.viewpagercards.ViewPagerCardsActivity
import com.gkzxhn.zoomheader.ZoomHeaderActivity
import kotlinx.android.synthetic.main.main_layout.*

/**
 * Created by Raleigh on 18/6/24.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var adapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        adapter= MainAdapter(this)
        adapter.setOnItemClickListener(onItemClickListener)
        mRecyclerView.adapter=adapter
        loadDatas()

    }
    fun loadDatas(){
        adapter.loadItem("MaterialLogin","转场动画Android 5.0以上")
        adapter.loadItem("RollingTextView","滚动文字")
        adapter.loadItem("ToggleButton","仿iOS开关按钮")
        adapter.loadItem("SupperCircle","可配置圆环")
        adapter.loadItem("SlantedTextView","斜角文字")
        adapter.loadItem("ZoomHeader","饿了么详情页可以跟随手指移动 ViewPager变详情页的效果")
        adapter.loadItem("LoadingView","在等待文字和图片加载时显示加载动画")
        adapter.loadItem("ViewPagerCards","卡片式ViewPager效果")
        adapter.loadItem("RingProgressBar","一个简单实现的自定义控件之MD风格的圆环进度条")
        adapter.loadItem("ScratchView","刮奖效果")

    }
    private val onItemClickListener=object :OnItemClickListener{
        override fun onClick(convertView: View, position: Int) {

            when(position){
                9->{
                    startActivity(Intent(this@MainActivity, ScratchDemoActivity::class.java))
                }
                8->{
                    startActivity(Intent(this@MainActivity, RingProgressBarActivity::class.java))
                }
                7->{
                    startActivity(Intent(this@MainActivity, ViewPagerCardsActivity::class.java))
                }
                6->{
                    startActivity(Intent(this@MainActivity, LoadingViewActivity::class.java))
                }
                5->{
                    startActivity(Intent(this@MainActivity, ZoomHeaderActivity::class.java))
                }
                4->{
                    startActivity(Intent(this@MainActivity, SlantedTextViewActivity::class.java))
                }
                3->{
                    startActivity(Intent(this@MainActivity, SuperCircleActivity::class.java))
                }
                2->{
                    startActivity(Intent(this@MainActivity, SwitchButtonActivity::class.java))
                }
                1->{
                    startActivity(Intent(this@MainActivity,RollingTextViewActivity::class.java))
                }
                0->{
                    startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                }

            }
        }
    }
}
