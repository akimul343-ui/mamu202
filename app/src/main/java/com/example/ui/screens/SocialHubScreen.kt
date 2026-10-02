package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.PlatformType
import com.example.ui.SkViewModel
import com.example.ui.components.SocialCard
import com.example.ui.theme.*

@Composable
fun SocialHubScreen(
    viewModel: SkViewModel,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("সব") }
    val filters = listOf("সব", "ফেসবুক", "টিকটক", "টেলিগ্রাম", "ইউটিউব", "ইনস্টাগ্রাম", "ফ্যান ক্লাব")

    val channels = viewModel.allSocialChannels.filter { channel ->
        when (selectedFilter) {
            "সব" -> true
            "ফেসবুক" -> channel.platform == PlatformType.FACEBOOK
            "টিকটক" -> channel.platform == PlatformType.TIKTOK
            "টেলিগ্রাম" -> channel.platform == PlatformType.TELEGRAM
            "ইউটিউব" -> channel.platform == PlatformType.YOUTUBE
            "ইনস্টাগ্রাম" -> channel.platform == PlatformType.INSTAGRAM
            "ফ্যান ক্লাব" -> channel.platform == PlatformType.COMMUNITY
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("social_hub_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Social Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sk_fan_community),
                    contentDescription = "শাকিবিয়ান সোশ্যাল কমিউনিটি",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.7f),
                                    DarkBackground
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPrimary,
                        border = BorderStroke(0.5.dp, Color.White)
                    ) {
                        Text(
                            text = "🌐 মেগাস্টার অফিশিয়াল নেটওয়ার্ক",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "ফেসবুক, টিকটক ও টেলিগ্রাম হাব",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark,
                            fontSize = 22.sp
                        )
                    )

                    Text(
                        text = "কিং খানের ভেরিফাইড সব সোশ্যাল চ্যানেল ও ভক্তদের নেটওয়ার্ক",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
            }
        }

        // Stats Summary Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialStatItem(label = "ফেসবুক", value = "৬.২M+", color = FacebookBlue)
                    VerticalDivider(modifier = Modifier.height(30.dp), color = DarkSurfaceCard)
                    SocialStatItem(label = "টিকটক", value = "৩.৮M+", color = TikTokPink)
                    VerticalDivider(modifier = Modifier.height(30.dp), color = DarkSurfaceCard)
                    SocialStatItem(label = "টেলিগ্রাম", value = "১.৮L+", color = TelegramBlue)
                    VerticalDivider(modifier = Modifier.height(30.dp), color = DarkSurfaceCard)
                    SocialStatItem(label = "ইউটিউব", value = "২.৫M+", color = YouTubeRed)
                }
            }
        }

        // Filter Chips Row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurfaceElevated,
                            labelColor = TextPrimaryDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) GoldPrimary else DarkSurfaceCard,
                            selectedBorderColor = GoldPrimary
                        )
                    )
                }
            }
        }

        // Channels List
        items(channels) { channel ->
            val isFav = viewModel.isItemFavorite(channel.id)
            SocialCard(
                channel = channel,
                isFavorite = isFav,
                onOpen = { viewModel.openUrl(channel.webUrl, channel.appIntentUri) },
                onCopy = { viewModel.copyToClipboard(channel.webUrl, channel.name) },
                onShare = {
                    viewModel.shareContent(
                        title = "${channel.name} (${channel.handle})",
                        text = "${channel.name}\n${channel.description}",
                        url = channel.webUrl
                    )
                },
                onToggleFavorite = { viewModel.toggleSocialFavorite(channel) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun SocialStatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 15.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondaryDark,
                fontSize = 11.sp
            )
        )
    }
}
