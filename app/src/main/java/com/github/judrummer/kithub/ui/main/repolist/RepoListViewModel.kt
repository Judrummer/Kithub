package com.github.judrummer.kithub.ui.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.repository.RepoRepository
import com.github.judrummer.kithub.data.repository.RepoRepositoryImpl
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by judrummer on 11/6/2560.
 */

data class RepoListState(val fetching: Boolean = false,
                         val refreshing: Boolean = false,
                         val repos: List<RepoItem> = listOf(),
                         val error: String? = null)

data class RepoItem(val id: String = "", val name: String = "", val description: String = "", val starCount: Int = 0) : JxItem

class RepoListViewIntent {
    val fetch = PublishSubject.create<Unit>()!!
    val refresh = PublishSubject.create<Unit>()!!
}

class RepoListViewModel(val repoRepository: RepoRepository = RepoRepositoryImpl()) : BaseViewModel() {

    val state: BehaviorSubject<RepoListState> = BehaviorSubject.createDefault(RepoListState())!!

    val viewIntent = RepoListViewIntent()

    private fun reducer(action: Action): RepoListState =
            when (action) {
                is Action.ReposFetching -> state.value.copy(fetching = true, error = null)
                is Action.ReposRefreshing -> state.value.copy(refreshing = true, error = null)
                is Action.ReposResponse -> state.value.copy(fetching = false, refreshing = false, repos = action.repos)
                is Action.ReposError -> state.value.copy(fetching = false, refreshing = false, error = action.error.message)
                else -> state.value
            }

    init {
        Observable.merge(viewIntent.fetch.map { true }, viewIntent.refresh.map { false })
                .switchMap { isFetch ->
                    repoRepository.getRepos()
                            .map { Action.ReposResponse(it.map(this::mapRepoToRepoItem)) as Action }
                            .onErrorReturn { Action.ReposError(it) }
                            .startWith(if (isFetch) Action.ReposFetching() else Action.ReposRefreshing())
                }.map(this::reducer).subscribe(state::onNext).addTo(disposables)
    }

    private fun mapRepoToRepoItem(repo: RepoEntity) = RepoItem(repo.id,
            repo.name,
            repo.description ?: "",
            repo.stargazers_count)

    private sealed class Action {
        class ReposRefreshing : Action()
        class ReposFetching : Action()
        class ReposResponse(val repos: List<RepoItem>) : Action()
        class ReposError(val error: Throwable) : Action()
    }

}