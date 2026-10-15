package com.ilyassavli.helloworld.ui.navigation

import androidx.navigation3.runtime.NavKey

/** ARCH-10: back never removes the root, so the NavDisplay can't end up empty. */
fun MutableList<NavKey>.goBack() {
    if (size > 1) removeAt(lastIndex)
}
