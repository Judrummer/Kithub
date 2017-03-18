package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.usecase.GetRepos
import com.github.judrummer.kithub.data.usecase.SearchRepos
import com.github.kittinunf.reactiveandroid.rx.addTo
import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import rx.Observable
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import org.junit.platform.runner.JUnitPlatform

/**
 * Created by judrummer on 19/3/2560.
 */

class RepoListViewIntentTester(testBlock: RepoListViewIntentTester.() -> (Unit)) : RepoListContract.ViewIntent {

    override val refreshIntent = PublishSubject.create<Unit>()
    override val searchIntent = PublishSubject.create<String>()
    override val subscriptions = CompositeSubscription()
    val getReposSubject = PublishSubject.create<Result<List<RepoEntity>, Exception>>()!!
    val searchReposSubject = PublishSubject.create<Result<List<RepoEntity>, Exception>>()!!
    val viewModel = RepoListViewModel(this,
            getRepos = { getReposSubject },
            searchRepos = { searchReposSubject })

    init {
        this.testBlock()
    }
}

@RunWith(JUnitPlatform::class)
class RepoListViewModelTest : Spek({
    describe("RepoListViewModel") {

        val GET_REPOS_RESPONSE = listOf(RepoEntity(id = "1", name = "repo1"),
                RepoEntity(id = "2", name = "repo2"))

        val SEARCH_REPOS_RESPONSE =
                listOf(RepoEntity(id = "1", name = "repo1"))
        on("trigger refresh and empty search") {
            it("getRepos correct") {
                RepoListViewIntentTester {
                    viewModel.attachView()
                    //prepare subscription
                    val reposSubscriber = TestSubscriber<List<RepoListContract.RepoItem>>()
                    val loadingSubscriber = TestSubscriber<Boolean>()
                    val errorSubscriber = TestSubscriber<Exception>()

                    viewModel.repos.subscribe(reposSubscriber).addTo(subscriptions)
                    viewModel.loading.subscribe(loadingSubscriber).addTo(subscriptions)
                    viewModel.error.subscribe(errorSubscriber).addTo(subscriptions)

                    refreshIntent.onNext(Unit)
                    searchIntent.onNext("")
                    getReposSubject.onNext(Result.of(GET_REPOS_RESPONSE))

                    assertEquals(reposSubscriber.onNextEvents, listOf(
                            listOf(
                                    RepoListContract.RepoItem(id = "1", name = "repo1"),
                                    RepoListContract.RepoItem(id = "2", name = "repo2")
                            )
                    ))

                    assertEquals(loadingSubscriber.onNextEvents, listOf(true, false))

                    assertEquals(errorSubscriber.onNextEvents, listOf<Exception>())

                    viewModel.detachView()
                    subscriptions.clear()
                }
            }
        }

        on("trigger refresh and text search") {
            it("searchRepos correct") {
                RepoListViewIntentTester {
                    viewModel.attachView()
                    //prepare subscription
                    val reposSubscriber = TestSubscriber<List<RepoListContract.RepoItem>>()
                    val loadingSubscriber = TestSubscriber<Boolean>()
                    val errorSubscriber = TestSubscriber<Exception>()

                    viewModel.repos.subscribe(reposSubscriber).addTo(subscriptions)
                    viewModel.loading.subscribe(loadingSubscriber).addTo(subscriptions)
                    viewModel.error.subscribe(errorSubscriber).addTo(subscriptions)

                    refreshIntent.onNext(Unit)
                    searchIntent.onNext("query")
                    searchReposSubject.onNext(Result.of(SEARCH_REPOS_RESPONSE))

                    assertEquals(reposSubscriber.onNextEvents, listOf(
                            listOf(
                                    RepoListContract.RepoItem(id = "1", name = "repo1")
                            )
                    ))

                    assertEquals(loadingSubscriber.onNextEvents, listOf(true, false))

                    assertEquals(errorSubscriber.onNextEvents, listOf<Exception>())

                    viewModel.detachView()
                    subscriptions.clear()
                }
            }
        }

    }
})