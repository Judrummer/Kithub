package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.extension.addTo
import com.github.kittinunf.result.Result
import io.reactivex.observers.TestObserver
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.lang.Exception

/**
 * Created by judrummer on 19/3/2560.
 */

class RepoListViewModelSpec : Spek({

    describe("RepoListViewModel") {
        var viewIntentRobot = RepoListViewIntentRobot()
        val GET_REPOS_RESPONSE = listOf(RepoEntity(id = "1", name = "repo1"),
                RepoEntity(id = "2", name = "repo2"))

        val SEARCH_REPOS_RESPONSE =
                listOf(RepoEntity(id = "1", name = "repo1"))
        beforeEachTest {
            viewIntentRobot = RepoListViewIntentRobot()
        }

        on("refresh ") {
            viewIntentRobot.run {
                val stateSubscriber = viewModel.state.test()
                val errorSubscriber = viewModel.showError.test()
                viewModel.state.subscribe {
                    println("StateChange $it")
                }.addTo(subscriptions)
                refreshIntent.onNext(Unit)
                getReposSubject.onNext(GET_REPOS_RESPONSE)
                it("get correct state") {
                    val expectedRepos = listOf(
                            RepoListContract.RepoItem(id = "1", name = "repo1"),
                            RepoListContract.RepoItem(id = "2", name = "repo2")
                    )
                    val expectedState1 = RepoListContract.State()
                    val expectedState2 = expectedState1.copy(loading = true)
                    val expectedState3 = expectedState2.copy(loading = false, repos = expectedRepos)
                    stateSubscriber.assertResult(expectedState1, expectedState2, expectedState3)
                }

                it("not contain any error") {
                    errorSubscriber.assertResult(Exception(""))
                }

            }

        }

//        on("refresh and text search") {
//            viewIntentRobot.run {
//                refreshIntent.onNext(Unit)
//                searchIntent.onNext("abc")
//                searchReposSubject.onNext(Result.of(SEARCH_REPOS_RESPONSE))
//
//                it("get correct repos") {
//                    assertEquals(listOf(
//                            listOf(
//                                    RepoListContract.RepoItem(id = "1", name = "repo1")
//                            )
//                    ), reposSubscriber.onNextEvents)
//                }
//                it("get correct loading") {
//                    assertEquals(listOf(true, false), loadingSubscriber.onNextEvents)
//                }
//                it("get correct error") {
//                    assertEquals(listOf<Exception>(), errorSubscriber.onNextEvents)
//                }
//
//            }
//
//        }
    }
})