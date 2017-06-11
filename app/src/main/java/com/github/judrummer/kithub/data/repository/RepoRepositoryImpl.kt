package com.github.judrummer.kithub.data.repository

import com.github.judrummer.kithub.data.RetrofitApi
import com.github.judrummer.kithub.data.api.RepoApi
import com.github.judrummer.kithub.data.entity.RepoEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by judrummer on 12/6/2560.
 */

class RepoRepositoryImpl : RepoRepository {

    override fun getRepos(): Observable<List<RepoEntity>> =
            RetrofitApi<RepoApi>().getRepos()
                    .delay(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}
