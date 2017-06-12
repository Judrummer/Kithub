package com.github.judrummer.kithub.ui.main

import android.os.Bundle
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.extension.transaction
import com.github.judrummer.kithub.ui.base.BaseActivity
import com.github.judrummer.kithub.ui.main.repodetail.RepoDetailFragment
import com.github.judrummer.kithub.ui.main.repolist.RepoListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), RepoListFragment.Listener {

    override val contentLayoutResourceId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.transaction { add(R.id.contentContainer, RepoListFragment()) }
        } else {
        }
    }

    override fun onRepoItemClick(id: String) {
        supportFragmentManager.transaction {
            add(R.id.contentContainer, RepoDetailFragment.instance(id))
            addToBackStack(null)
        }
    }

}
