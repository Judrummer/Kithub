package com.github.judrummer.kithub.data.entity

import com.google.gson.annotations.SerializedName
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
        @SerializedName("full_name") open var fullName: String = "",
        @SerializedName("stargazers_count") open var stargazersCount: Int = 0) : RealmModel
