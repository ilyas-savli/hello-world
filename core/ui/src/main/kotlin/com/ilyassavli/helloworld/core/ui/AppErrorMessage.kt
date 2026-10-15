package com.ilyassavli.helloworld.core.ui

import androidx.annotation.StringRes
import com.ilyassavli.helloworld.core.common.result.AppError
import com.ilyassavli.helloworld.core.i18n.R

/** The one place that turns a typed error into user-facing text. */
@StringRes
fun AppError.messageRes(): Int = when (this) {
    AppError.Network -> R.string.common_error_network
    is AppError.Server -> R.string.common_error_server
    AppError.Unknown -> R.string.common_error_unknown
}
