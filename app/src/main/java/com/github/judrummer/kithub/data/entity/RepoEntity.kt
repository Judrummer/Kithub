package com.github.judrummer.kithub.data.entity

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by judrummer on 19/3/2560.
 */

@RealmClass
open class RepoEntity(
        @PrimaryKey open var id: String = "",
        open var name: String = "",
        open var description: String? = "",
        open var full_name: String = "",
        open var stargazers_count: Int = 0) : RealmModel
