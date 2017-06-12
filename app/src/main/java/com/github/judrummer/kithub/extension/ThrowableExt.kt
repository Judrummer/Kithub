package com.github.judrummer.kithub.extension

import android.content.Context
import com.github.judrummer.kithub.R
import java.net.UnknownHostException

/**
 * Created by judrummer on 12/6/2560.
 */

fun Throwable.errorMessageResId(context: Context) = when (this) {
    is UnknownHostException -> R.string.throwable_error_unknown_host
    else -> R.string.throwable_error_common
}
