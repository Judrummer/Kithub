package com.github.judrummer.kithub.data.api

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by judrummer on 9/5/2560.
 */

data class SearchReposApiResponse(@SerializedName("total_count") val totalCount: Int,
                                  @SerializedName("incomplete_results") val incompleteResults: Boolean,
                                  @SerializedName("items") val items: List<RepoEntity>)

interface RepoApi {

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int): Observable<SearchReposApiResponse>

}

