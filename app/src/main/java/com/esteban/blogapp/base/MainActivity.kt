package com.esteban.blogapp.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esteban.blogapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}