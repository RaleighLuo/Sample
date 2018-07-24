package com.gkzxhn.materiallogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import cn.fanrunqi.materiallogin.R

class LoginSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_success_layout)
        val explode = Explode()
        explode.duration = 500
        window.exitTransition = explode
        window.enterTransition = explode
    }
}