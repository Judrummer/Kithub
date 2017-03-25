package com.github.judrummer.kithub.app.main

import android.os.Bundle
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.app.main.repolist.RepoListFragment
import com.github.judrummer.kithub.base.BaseActivity
import com.github.judrummer.kithub.extension.transaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val contentLayoutResourceId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportFragmentManager.transaction { add(R.id.contentContainer, RepoListFragment()) }
    }

}
