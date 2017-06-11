package com.github.judrummer.kithub.data.entity

/**
 * Created by judrummer on 12/6/2560.
 */

data class RepositoryResult<out T>(val response: T, val apiStatus: ApiStatus)