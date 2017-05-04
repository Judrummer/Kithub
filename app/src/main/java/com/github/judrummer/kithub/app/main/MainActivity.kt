package com.github.judrummer.kithub.app.main

import android.os.Bundle
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.app.main.repodetail.RepoDetailFragment
import com.github.judrummer.kithub.app.main.repolist.RepoListContract
import com.github.judrummer.kithub.app.main.repolist.RepoListFragment
import com.github.judrummer.kithub.base.BaseActivity
import com.github.judrummer.kithub.extension.transaction
import com.taskworld.kxandroid.logD
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),RepoListContract.Listener {

    override val contentLayoutResourceId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.transaction { add(R.id.contentContainer, RepoListFragment()) }
        } else {
        }
    }

    override fun onRepoItemClick(repo: RepoListContract.RepoItem) {
        supportFragmentManager.transaction {
            add(R.id.contentContainer, RepoDetailFragment.instance(repo))
            addToBackStack(null)
        }
    }

}
