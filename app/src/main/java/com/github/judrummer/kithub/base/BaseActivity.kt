package com.github.judrummer.kithub.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu


abstract class BaseActivity : AppCompatActivity() {


    abstract val contentLayoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (contentLayoutResourceId != 0) {
            setContentView(contentLayoutResourceId)
        }
    }
}