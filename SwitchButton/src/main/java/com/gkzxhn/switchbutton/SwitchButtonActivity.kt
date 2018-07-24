package com.gkzxhn.switchbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.switch_button_layout.*

class SwitchButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.switch_button_layout)
		switchButton.setChecked(true);
		switchButton.isChecked();
		switchButton.toggle();     //switch state
		switchButton.toggle(false);//switch without animation
		switchButton.setShadowEffect(true);//disable shadow effect
		switchButton.setEnabled(false);//disable button
		switchButton.setEnableEffect(false);//disable the switch animation
		switchButton.setOnCheckedChangeListener(object :SwitchButton.OnCheckedChangeListener{
			override fun onCheckedChanged(view: SwitchButton, isChecked: Boolean) {
			}

		})
    }
}