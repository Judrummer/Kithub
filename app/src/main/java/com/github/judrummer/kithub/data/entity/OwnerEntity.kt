package com.github.judrummer.kithub.data.entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by judrummer on 13/6/2560.
 */

@RealmClass
open class OwnerEntity(
        @PrimaryKey open var id: String = "",
        open var login: String = "",
        @SerializedName("avatar_url") var avatarUrl: String = ""
) : RealmModel