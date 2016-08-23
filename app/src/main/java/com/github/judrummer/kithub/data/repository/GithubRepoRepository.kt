package com.github.judrummer.kithub.data.repository

import com.github.judrummer.kithub.data.model.Repo
import com.github.kittinunf.result.Result
import rx.Observable

interface GithubRepoRepository {
    fun getRepoes(): Observable<Result<List<Repo>, Exception>>
}

