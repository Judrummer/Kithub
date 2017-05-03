package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.extension.addTo
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by judrummer on 2/5/2560.
 */


class RepoListViewModelTest {

    val GET_REPOS_RESPONSE = listOf(RepoEntity(id = "1", name = "repo1"),
            RepoEntity(id = "2", name = "repo2"))

    lateinit var viewIntentRobot: RepoListViewIntentRobot

    @Before
    fun setup() {
        viewIntentRobot = RepoListViewIntentRobot()
    }

    @Test
    fun refresh() {
        viewIntentRobot.run {
            println("Run Test")
            viewModel.state.subscribe {
                println("CurrentState $it")
            }
            println("AfterSubscribe")
            refreshIntent.onNext(Unit)
            println("Refresh")
            getReposSubject.onNext(GET_REPOS_RESPONSE)
            println("APIREPO")
            val expectedRepos = listOf(
                    RepoListContract.RepoItem(id = "1", name = "repo1"),
                    RepoListContract.RepoItem(id = "2", name = "repo2")
            )
            val expectedState1 = RepoListContract.State()
            val expectedState2 = expectedState1.copy(loading = true)
            val expectedState3 = expectedState2.copy(loading = false, repos = expectedRepos)
            println("Value ${stateObserver.values().size} ${stateObserver.values()}")
            stateObserver.assertValues(expectedState1, expectedState2, expectedState3)
            showErrorObserver.assertEmpty()
        }
    }


}