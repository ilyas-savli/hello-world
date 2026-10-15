package com.ilyassavli.helloworld.feature.hello.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ilyassavli.helloworld.feature.hello.HelloScreen
import kotlinx.serialization.Serializable

@Serializable
data object HelloKey : NavKey

fun EntryProviderScope<NavKey>.helloEntry() {
    entry<HelloKey> {
        HelloScreen()
    }
}
