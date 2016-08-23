package com.github.judrummer.kithub.data.repository

import com.github.judrummer.kithub.data.model.Repo
import com.github.kittinunf.result.Result
import rx.Observable
import java.util.concurrent.TimeUnit

class GithubRepoRepositoryImpl() : GithubRepoRepository {

    override fun getRepoes(): Observable<Result<List<Repo>, Exception>> = Observable.just(Result.of(listOf(
            Repo("1", "RxAndroid"),
            Repo("2", "RxJava"),
            Repo("3", "RxKotlin")
    )))

}

