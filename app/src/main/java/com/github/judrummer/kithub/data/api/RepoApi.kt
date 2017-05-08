package com.github.judrummer.kithub.data.api

import com.github.judrummer.kithub.data.entity.RepoEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by judrummer on 9/5/2560.
 */

interface RepoApi {

    @GET("repositories")
    fun getRepos(): Observable<List<RepoEntity>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): Observable<List<RepoEntity>>

}

