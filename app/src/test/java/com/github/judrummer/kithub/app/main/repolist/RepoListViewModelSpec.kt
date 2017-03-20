package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.kittinunf.result.Result
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by judrummer on 19/3/2560.
 */

object RepoListViewModelSpec : Spek({

    describe("RepoListViewModel") {
        var viewIntentRobot = RepoListViewIntentRobot()
        val GET_REPOS_RESPONSE = listOf(RepoEntity(id = "1", name = "repo1"),
                RepoEntity(id = "2", name = "repo2"))

        val SEARCH_REPOS_RESPONSE =
                listOf(RepoEntity(id = "1", name = "repo1"))
        beforeEachTest {
            viewIntentRobot = RepoListViewIntentRobot()
        }

        on("refresh and empty search") {
            viewIntentRobot.run {
                refreshIntent.onNext(Unit)
                searchIntent.onNext("")
                getReposSubject.onNext(Result.of(GET_REPOS_RESPONSE))

                it("get correct repos") {
                    assertEquals(listOf(
                            listOf(
                                    RepoListContract.RepoItem(id = "1", name = "repo1"),
                                    RepoListContract.RepoItem(id = "2", name = "repo2")
                            )
                    ), reposSubscriber.onNextEvents)
                }
                it("get correct loading") {
                    assertEquals(listOf(true, false), loadingSubscriber.onNextEvents)
                }
                it("get correct error") {
                    assertEquals(listOf<Exception>(), errorSubscriber.onNextEvents)
                }

            }

        }

        on("refresh and text search") {
            viewIntentRobot.run {
                refreshIntent.onNext(Unit)
                searchIntent.onNext("abc")
                searchReposSubject.onNext(Result.of(SEARCH_REPOS_RESPONSE))

                it("get correct repos") {
                    assertEquals(listOf(
                            listOf(
                                    RepoListContract.RepoItem(id = "1", name = "repo1")
                            )
                    ), reposSubscriber.onNextEvents)
                }
                it("get correct loading") {
                    assertEquals(listOf(true, false), loadingSubscriber.onNextEvents)
                }
                it("get correct error") {
                    assertEquals(listOf<Exception>(), errorSubscriber.onNextEvents)
                }

            }

        }
    }
})