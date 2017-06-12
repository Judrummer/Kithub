package com.github.judrummer.kithub.ui.main.repolist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.extension.errorMessage
import com.github.judrummer.kithub.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_repo_list.*
import kotlinx.android.synthetic.main.item_repo.view.*

/**
 * Created by judrummer on 12/6/2560.
 */

class RepoListFragment : BaseFragment() {

    interface Listener {
        fun onRepoItemClick(id: String)
    }

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_list

    val viewModel: RepoListViewModel by lazy { ViewModelProviders.of(this@RepoListFragment).get(RepoListViewModel::class.java) }

    val repoAdapter by lazy {
        JxAdapter(JxViewHolder<RepoItem>(R.layout.item_repo) { _, repo ->
            itemView.apply {
                tvItemStar.text = repo.starCount.toString()
                tvItemName.text = repo.name
                setOnClickListener {
                    val parentActivity = activity
                    if (parentActivity is Listener) {
                        parentActivity.onRepoItemClick(repo.id)
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewIntent = viewModel.viewIntent

        srlRepoList.setOnRefreshListener { viewIntent.refresh.onNext(Unit) }
        rvRepoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoAdapter
        }

        viewModel.state.subscribe { state ->
            repoAdapter.items = state.repos
            srlRepoList.isRefreshing = state.refreshing
            if (state.fetching) {
                srlRepoList.isEnabled = false
                pbRepoList.visibility = View.VISIBLE
            } else {
                srlRepoList.isEnabled = true
                pbRepoList.visibility = View.GONE
            }
            if (state.error != null) {
                tvRepoListError.visibility = View.VISIBLE
                tvRepoListError.text = state.error.errorMessage(context)
            } else {
                tvRepoListError.visibility = View.GONE
            }
        }.addTo(disposables)

        viewModel.showErrorDialog.subscribe { error ->
            Snackbar.make(rootView, error.errorMessage(context), Snackbar.LENGTH_SHORT).apply {
                (this.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).maxLines = 3
                setAction(R.string.snackbar_action_dismiss) { this.dismiss() }
            }.show()
        }.addTo(disposables)

        if (savedInstanceState == null) viewIntent.fetch.onNext(Unit)
    }

}