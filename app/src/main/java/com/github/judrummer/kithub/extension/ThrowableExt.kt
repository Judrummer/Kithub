package com.github.judrummer.kithub.extension

import android.content.Context
import com.github.judrummer.kithub.R
import java.net.UnknownHostException

/**
 * Created by judrummer on 12/6/2560.
 */

fun Throwable.errorMessage(context: Context) = when (this) {
    is UnknownHostException -> context.getString(R.string.throwable_error_unknown_host)
    else -> context.getString(R.string.throwable_error_common) + " " + this.message
}
