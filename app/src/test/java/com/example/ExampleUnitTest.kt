package com.example

import com.example.data.models.PlatformType
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPlatformTypes() {
        val platforms = PlatformType.values()
        assertTrue(platforms.contains(PlatformType.FACEBOOK))
        assertTrue(platforms.contains(PlatformType.TIKTOK))
        assertTrue(platforms.contains(PlatformType.TELEGRAM))
        assertTrue(platforms.contains(PlatformType.YOUTUBE))
    }
}
