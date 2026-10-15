package com.ilyassavli.helloworld.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlin.test.assertEquals
import org.junit.Test

class BackStackNavigationTest {

    private data object Home : NavKey

    private data object Detail : NavKey

    @Test
    fun `when going back, then the top key is removed`() {
        val backStack = mutableListOf(Home, Detail)

        backStack.goBack()

        assertEquals(listOf<NavKey>(Home), backStack)
    }

    @Test
    fun `when going back on the root, then the root stays`() {
        val backStack = mutableListOf<NavKey>(Home)

        backStack.goBack()

        assertEquals(listOf<NavKey>(Home), backStack)
    }
}
