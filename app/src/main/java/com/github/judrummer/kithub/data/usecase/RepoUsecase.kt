package com.github.judrummer.kithub.data.usecase

import com.github.judrummer.kithub.extension.mapResult
import com.github.judrummer.kithub.extension.rx_gson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import rx.Observable

/**
 * Created by judrummer on 18/3/2560.
 */

typealias  GetRepos = () -> (Observable<Result<List<RepoEntity>, Exception>>)
typealias  SearchRepos = (String) -> (Observable<Result<List<RepoEntity>, Exception>>)

data class RepoEntity(val id: String,
                      val name: String,
                      val description: String?,
                      val full_name: String,
                      val stargazers_count: Int)

data class SearchReposApiResponse(val items: List<RepoEntity>)

val GetReposImpl: GetRepos = {
    "https://api.github.com/repositories".httpGet()
            .rx_gson<List<RepoEntity>>()
            .mapResult()
}


val SearchReposImpl: SearchRepos = { query: String ->
    val q = query
    "https://api.github.com/search/repositories?q=$q".httpGet()
            .rx_gson<SearchReposApiResponse>()
            .map { it.items }
            .mapResult()
}


