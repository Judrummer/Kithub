package com.github.judrummer.kithub

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by judrummer on 19/3/2560.
 */


class KithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

}