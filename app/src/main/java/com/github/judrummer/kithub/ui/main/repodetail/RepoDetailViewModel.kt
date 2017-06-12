package com.github.judrummer.kithub.ui.main.repodetail

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.repository.RepoRepository
import com.github.judrummer.kithub.data.repository.RepoRepositoryImpl
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.ui.base.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by judrummer on 12/6/2560.
 */

data class RepoDetailState(val repo: RepoDetail = RepoDetail(), val error: Throwable? = null, val fetching: Boolean = false)

data class RepoDetail(val id: String = "", val name: String = "", val description: String = "", val starCount: Int = 0)

class RepoDetailViewIntent {
    val repoId = PublishSubject.create<String>()
}

class RepoDetailViewModel(val repoRepository: RepoRepository = RepoRepositoryImpl()) : BaseViewModel() {

    val state = BehaviorSubject.createDefault(RepoDetailState())
    val viewIntent = RepoDetailViewIntent()

    init {
        viewIntent.repoId.switchMap {
            repoRepository.getRepo(it)
                    .map { Action.RepoResponse(RepoDetail(id = it.id, name = it.name, description = it.description ?: "", starCount = it.stargazersCount)) as Action }
                    .onErrorReturn { Action.RepoError(it) }
                    .startWith(Action.RepoFetching)
        }.map(this::reducer).subscribe(state::onNext).addTo(disposables)
    }

    private fun reducer(action: Action): RepoDetailState = when (action) {
        is Action.RepoFetching -> state.value.copy(fetching = true, error = null)
        is Action.RepoResponse -> state.value.copy(fetching = false, error = null, repo = action.repo)
        is Action.RepoError -> state.value.copy(fetching = false, error = action.error)
    }

    private sealed class Action {
        object RepoFetching : Action()
        class RepoResponse(val repo: RepoDetail) : Action()
        class RepoError(val error: Throwable) : Action()
    }

}