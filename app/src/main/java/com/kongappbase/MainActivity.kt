package com.kongappbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kong.project.base.util.UtilUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("kong", "dp2px----${UtilUnit.dp2Px(this, 20f)}")
    }
}