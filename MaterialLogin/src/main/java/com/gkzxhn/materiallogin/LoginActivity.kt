package com.gkzxhn.materiallogin

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.util.Log
import android.util.Pair
import android.view.View
import cn.fanrunqi.materiallogin.R
import kotlinx.android.synthetic.main.login_layout.login_layout_fab_register
as fabRegister
import kotlinx.android.synthetic.main.login_layout.login_layout_et_username
as etUsername
import kotlinx.android.synthetic.main.login_layout.login_layout_et_password
as etPassword
import kotlinx.android.synthetic.main.login_layout.login_layout_btn_go
as btnGo
import kotlinx.android.synthetic.main.login_layout.login_layout_fab_register
as fabRegister
class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

    }
    fun onClick(view: View){
        if(view.id==R.id.login_layout_btn_go){
            //Explode从场景中心移入或移出视图
            //Slide() 滑动效果
            //Fade() 淡入淡出动画效果
            val explode = Explode()
            explode.duration = 500
            window.exitTransition = explode
            window.enterTransition = explode
            val oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
            val i2 = Intent(this@LoginActivity, LoginSuccessActivity::class.java)
            startActivity(i2, oc2.toBundle())
        }else if(view.id==R.id.login_layout_fab_register){
            //设置无转场动画
            //exitTransition: 当A start B时，使A中的View退出场景的transition    在A中设置
            //enterTransition: 当A start B时，使B中的View进入场景的transition    在B中设置
            //returnTransition: 当B 返回 A时，使B中的View退出场景的transition   在B中设置
            //reenterTransition: 当B 返回 A时，使A中的View进入场景的transition   在A中设置
            window.exitTransition = null
            window.enterTransition = null
            //transitionName 共享元素动画
            //Activity和跳转的Activity中的两个button,分别添加相同值android:transitionName=”login”,这样系统才知道这两个控件是共享元素
            val options = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity, fabRegister, fabRegister.getTransitionName())
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java), options.toBundle())

            //两个页面多个共享元素
//            ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity, Pair.create(view1,"name1"), Pair.create(view2,"name2") )
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("raleigh_test","onRestart")
        fabRegister.visibility=View.GONE
    }

    override fun onResume() {
        super.onResume()
        Log.e("raleigh_test","onResume")
        fabRegister.visibility=View.VISIBLE
    }
}
