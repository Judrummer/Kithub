package com.github.judrummer.kithub.data.db

import com.github.judrummer.kithub.data.entity.RepoEntity
import io.reactivex.Observable
import io.realm.Realm

/**
 * Created by judrummer on 12/6/2560.
 */

class RepoDbImpl : RepoDb {

    override fun getRepos(): Observable<List<RepoEntity>> {
        val repos = mutableListOf<RepoEntity>()
        Realm.getDefaultInstance().use { realm ->
            repos.addAll(realm.copyFromRealm(realm.where(RepoEntity::class.java).findAll()))
        }
        return Observable.just(repos)
    }

    override fun saveRepos(repos: List<RepoEntity>) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.copyToRealmOrUpdate(repos)
            }
        }
    }

    override fun getRepo(id: String): Observable<RepoEntity> {
        var repo: RepoEntity? = null
        Realm.getDefaultInstance().use { realm ->
            repo = realm.where(RepoEntity::class.java).equalTo("id", id).findFirst()
        }
        return if (repo != null) Observable.just(repo) else Observable.error(NullPointerException("Not found id $id"))
    }

}
