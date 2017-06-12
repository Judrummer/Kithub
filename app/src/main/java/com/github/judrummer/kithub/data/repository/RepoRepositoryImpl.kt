package com.github.judrummer.kithub.data.repository

import com.github.judrummer.kithub.data.RetrofitApi
import com.github.judrummer.kithub.data.entity.ApiStatus
import com.github.judrummer.kithub.data.api.RepoApi
import com.github.judrummer.kithub.data.db.RepoDb
import com.github.judrummer.kithub.data.db.RepoDbImpl
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.entity.RepositoryResult
import com.github.judrummer.kithub.extension.kx_combineLatest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by judrummer on 12/6/2560.
 */

class RepoRepositoryImpl(val repoApi: RepoApi = RetrofitApi<RepoApi>(),
                         val repoDb: RepoDb = RepoDbImpl()) : RepoRepository {

    override fun getRepos(): Observable<RepositoryResult<List<RepoEntity>>> {
        val repoApiStatus = repoApi.searchRepos("kotlin", 1, 50)  //TODO: handle pagination
                .map { it.items }
                .doOnNext { repoDb.saveRepos(it) }
                .map { ApiStatus.Success as ApiStatus }
                .onErrorReturn { ApiStatus.Error(it) }
                .startWith(ApiStatus.Loading)
        return kx_combineLatest(repoDb.getRepos(), repoApiStatus) { db, apiStatus ->
            when (apiStatus) {
                is ApiStatus.Success -> RepositoryResult(repoDb.getRepos().blockingFirst(), apiStatus)
                else -> RepositoryResult(db, apiStatus)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRepo(id: String): Observable<RepoEntity> = repoDb.getRepo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
