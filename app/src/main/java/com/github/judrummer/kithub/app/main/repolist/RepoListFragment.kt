package com.github.judrummer.kithub.app.main.repolist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.jxadapter.JxViewHolder

import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.app.main.repodetail.RepoDetailFragment
import com.github.judrummer.kithub.base.BaseFragment
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.extension.parseJson
import com.github.judrummer.kithub.extension.toJson
import com.github.judrummer.kithub.extension.transaction

import com.taskworld.kxandroid.support.v4.toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_repo_list.*
import kotlinx.android.synthetic.main.item_repo.view.*


class RepoListFragment : BaseFragment(), RepoListContract.ViewIntent {

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_list
    override val refreshIntent = PublishSubject.create<Unit>()!!

    val subscriptions = CompositeDisposable()
    val viewModel: RepoListContract.ViewModel by lazy { RepoListViewModel(this) }
    val repoAdapter by lazy {
        JxAdapter(JxViewHolder<RepoListContract.RepoItem>(R.layout.item_repo) { _, repo ->
            itemView.apply {
                tvItemStar.text = repo.starCount.toString()
                tvItemName.text = repo.name
                setOnClickListener {
                    val parentActivity = activity
                    if(parentActivity is RepoListContract.Listener){
                        parentActivity.onRepoItemClick(repo)
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.attachView()
        srlRepoList.setOnRefreshListener { refreshIntent.onNext(Unit) }
        rvRepoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoAdapter
        }
        viewModel.state.subscribe { state ->
            repoAdapter.items = state.repos
            srlRepoList.isRefreshing = state.loading
        }.addTo(subscriptions)

        viewModel.showError.subscribe {
            toast("Error ${it.message ?: ""}")
        }.addTo(subscriptions)


        if (savedInstanceState == null) {
            refreshIntent.onNext(Unit)
        } else {
            viewModel.restoreState(savedInstanceState.getString(RepoListViewModel::class.java.simpleName).parseJson())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        viewModel.detachView()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(RepoListViewModel::class.java.simpleName, viewModel.saveState().toJson())
    }

}

