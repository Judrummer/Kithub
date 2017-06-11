package com.github.judrummer.kithub.ui.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.data.entity.ApiStatus
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.repository.RepoRepository
import com.github.judrummer.kithub.data.repository.RepoRepositoryImpl
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.ui.base.BaseViewModel
import com.taskworld.kxandroid.logD
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
    val showErrorDialog = PublishSubject.create<String>()!!
    val viewIntent = RepoListViewIntent()

    private fun reducer(action: Action): RepoListState =
            when (action) {
                is Action.ReposFetching -> state.value.copy(fetching = true, error = null)
                is Action.ReposRefreshing -> state.value.copy(refreshing = true, error = null)
                is Action.ReposResponse -> {
                    if (action.apiStatus is ApiStatus.Error) showErrorDialog.onNext(action.apiStatus.error.message)
                    state.value.copy(fetching = false, refreshing = false, repos = action.repos, error = null)
                }
                is Action.ReposError -> state.value.copy(fetching = false, refreshing = false, repos = listOf(), error = action.error.message)
                else -> state.value
            }

    init {
        Observable.merge(viewIntent.fetch.map { true }, viewIntent.refresh.map { false })
                .switchMap { isFetch ->
                    logD(isFetch)
                    repoRepository.getRepos()
                            .map { result ->
                                when (result.apiStatus) {
                                    is ApiStatus.Loading -> if (isFetch) Action.ReposFetching else Action.ReposRefreshing
                                    else -> Action.ReposResponse(result.response.map(this::mapRepoToRepoItem), result.apiStatus)
                                } as Action
                            }
                            .onErrorReturn { Action.ReposError(it) }
                }.map(this::reducer).subscribe(state::onNext).addTo(disposables)
    }

    private fun mapRepoToRepoItem(repo: RepoEntity) = RepoItem(repo.id,
            repo.name,
            repo.description ?: "",
            repo.stargazers_count)

    private sealed class Action {

        object ReposRefreshing : Action()
        object ReposFetching : Action()
        class ReposResponse(val repos: List<RepoItem>, val apiStatus: ApiStatus) : Action()
        class ReposError(val error: Throwable) : Action()
    }

}