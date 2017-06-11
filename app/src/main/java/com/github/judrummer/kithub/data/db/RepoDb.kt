package com.github.judrummer.kithub.data.db

import com.github.judrummer.kithub.data.entity.RepoEntity
import io.reactivex.Observable

/**
 * Created by judrummer on 12/6/2560.
 */

interface RepoDb {
    fun saveRepos(repos: List<RepoEntity>)
    fun getRepos(): Observable<List<RepoEntity>>
}