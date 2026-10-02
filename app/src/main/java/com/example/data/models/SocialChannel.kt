package com.example.data.models

import androidx.compose.ui.graphics.Color

enum class PlatformType {
    FACEBOOK,
    TIKTOK,
    TELEGRAM,
    YOUTUBE,
    INSTAGRAM,
    TWITTER,
    COMMUNITY
}

data class SocialChannel(
    val id: String,
    val platform: PlatformType,
    val name: String,
    val handle: String,
    val followers: String,
    val description: String,
    val webUrl: String,
    val appIntentUri: String? = null,
    val isVerified: Boolean = true,
    val badgeColor: Long,
    val category: String = "অফিসিয়াল"
)
