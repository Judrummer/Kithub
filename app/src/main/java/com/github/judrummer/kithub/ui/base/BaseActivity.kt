package com.github.judrummer.kithub.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    abstract val contentLayoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (contentLayoutResourceId != 0) {
            setContentView(contentLayoutResourceId)
        }
    }

}