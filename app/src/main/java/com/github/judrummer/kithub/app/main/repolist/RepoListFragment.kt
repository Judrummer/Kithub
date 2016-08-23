package com.github.judrummer.kithub.app.main.repolist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.base.BaseFragment
import com.github.judrummer.kithub.data.repository.GithubRepoRepositoryImpl
import com.github.kittinunf.reactiveandroid.scheduler.AndroidThreadScheduler
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_queryTextChange
import com.taskworld.kxandroid.support.v4.toast
import com.taskworld.kxandroid.unSafeLazy
import kotlinx.android.synthetic.main.fragment_repo_list.*
import kotlinx.android.synthetic.main.item_repo.view.*
import rx.Observable
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RepoListFragment : BaseFragment(), RepoListContract.View {

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_list
    val searchSubject = PublishSubject.create<String>()
    val refreshSubject = PublishSubject.create<Unit>()
    val viewModel by unSafeLazy { RepoListViewModel(this, GithubRepoRepositoryImpl()) }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.attachView()
        srlRepoList.setOnRefreshListener { refreshSubject.onNext(Unit) }
        rvRepoList.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rx_itemsWith(viewModel.repoes.untilDestroy(),
                    { parent, viewType ->
                        RepoItemViewHolder(view = LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false))
                    },
                    { viewHolder, position, repo ->
                        viewHolder.itemView.apply {
                            tvItemId.text = repo.id
                            tvItemName.text = repo.name
                        }
                    })
        }
        refreshSubject.onNext(Unit)
        searchSubject.onNext("")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
        searchView.rx_queryTextChange(false)
                .untilDestroyView()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidThreadScheduler.main)
                .subscribe { searchSubject.onNext(it) }
    }

    override fun refreshIntent(): Observable<Unit> = refreshSubject.asObservable().untilDestroyView()

    override fun searchIntent(): Observable<String> = searchSubject.asObservable().untilDestroyView()

    override fun showProgressBar() {
        srlRepoList.isRefreshing = true
    }

    override fun hideProgressBar() {
        srlRepoList.isRefreshing = false
    }

    override fun showError(error: Throwable) {
        toast("Error ${error.message ?: ""}")
    }

    class RepoItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

