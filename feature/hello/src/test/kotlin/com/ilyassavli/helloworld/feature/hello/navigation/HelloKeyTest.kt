package com.ilyassavli.helloworld.feature.hello.navigation

import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

class HelloKeyTest {
    @Test
    fun `key survives a serialization round trip`() {
        val restored = Json.decodeFromString<HelloKey>(Json.encodeToString(HelloKey))

        assertEquals(HelloKey, restored)
    }
}
