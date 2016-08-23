package com.github.judrummer.kithub.app.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem

import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.app.main.repolist.RepoListFragment
import com.github.judrummer.kithub.base.BaseActivity
import com.github.judrummer.kithub.extension.fragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val contentLayoutResourceId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        fragmentTransaction { add(R.id.contentContainer, RepoListFragment()) }
    }

}
