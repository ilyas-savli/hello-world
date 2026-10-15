package com.ilyassavli.helloworld.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ilyassavli.helloworld.feature.hello.navigation.HelloKey
import com.ilyassavli.helloworld.feature.hello.navigation.helloEntry
import com.ilyassavli.helloworld.ui.navigation.goBack

/**
 * Root composable: owns the Navigation 3 back stack and wires feature entries together. Features never touch the
 * back stack — they expose callbacks and this file connects them (ARCH-01, ARCH-08). Go back with
 * `backStack.goBack()` (ARCH-10); add a `navigateTo` helper together with the second screen.
 */
@Composable
internal fun HelloWorldApp(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HelloKey)
    NavDisplay(
        backStack = backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { backStack.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // Scopes each entry's ViewModels to the entry, so they are cleared when it leaves the back stack.
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            helloEntry()
        },
    )
}
