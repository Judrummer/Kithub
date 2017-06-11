package com.github.judrummer.kithub.data.entity

/**
 * Created by judrummer on 12/6/2560.
 */

sealed class ApiStatus {
    object Loading : ApiStatus()
    object Success : ApiStatus()
    class Error(val error: Throwable) : ApiStatus()
}

