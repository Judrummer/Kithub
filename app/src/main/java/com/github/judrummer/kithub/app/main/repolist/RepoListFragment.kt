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
import com.github.judrummer.jxadapter.JxViewHolder
import com.github.judrummer.jxadapter_rxjava.rx_jxAdapter
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.base.BaseFragment
import com.github.kittinunf.reactiveandroid.rx.addTo
import com.github.kittinunf.reactiveandroid.scheduler.AndroidThreadScheduler
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_queryTextChange
import com.taskworld.kxandroid.logD
import com.taskworld.kxandroid.support.v4.toast
import kotlinx.android.synthetic.main.fragment_repo_list.*
import kotlinx.android.synthetic.main.item_repo.view.*
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class RepoListFragment : BaseFragment(), RepoListContract.ViewIntent {

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_list
    override val searchIntent = PublishSubject.create<String>()!!
    override val refreshIntent = PublishSubject.create<Unit>()!!
    override val subscriptions = CompositeSubscription()

    val viewModel: RepoListContract.ViewModel by lazy { RepoListViewModel(this) }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.attachView()
        srlRepoList.setOnRefreshListener { refreshIntent.onNext(Unit) }
        rvRepoList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rx_jxAdapter(viewModel.repos.map { it as List<Any> },
                    JxViewHolder<RepoListContract.RepoItem>(R.layout.item_repo) { _, (id, name, description, starCount) ->
                        itemView.apply {
                            tvItemStar.text = starCount.toString()
                            tvItemName.text = name
                            setOnClickListener {
                                toast("Click Repo[$id] -> $name $description")
                            }
                        }
                    }).addTo(subscriptions)
        }

        viewModel.loading.subscribe {
            srlRepoList.isRefreshing = it
        }.addTo(subscriptions)

        viewModel.error.subscribe {
            toast("Error ${it.message ?: ""}")
        }.addTo(subscriptions)


        refreshIntent.onNext(Unit)
        searchIntent.onNext("")
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        viewModel.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
            rx_queryTextChange(false)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidThreadScheduler.main)
                    .subscribe { searchIntent.onNext(it) }.addTo(subscriptions)
        }
    }

}

