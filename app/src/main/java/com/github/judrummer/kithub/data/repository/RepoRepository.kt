package com.github.judrummer.kithub.data.repository

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.entity.RepositoryResult
import io.reactivex.Observable

/**
 * Created by judrummer on 12/6/2560.
 */
interface RepoRepository {
    fun getRepos(): Observable<RepositoryResult<List<RepoEntity>>>
    fun getRepo(id: String): Observable<RepoEntity>
}
