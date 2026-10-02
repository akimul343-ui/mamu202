package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PlatformType
import com.example.data.models.SocialChannel
import com.example.ui.theme.*

@Composable
fun SocialCard(
    channel: SocialChannel,
    isFavorite: Boolean,
    onOpen: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val platformColor = Color(channel.badgeColor)
    val iconVector: ImageVector = when (channel.platform) {
        PlatformType.FACEBOOK -> Icons.Default.Public
        PlatformType.TIKTOK -> Icons.Default.MusicVideo
        PlatformType.TELEGRAM -> Icons.Default.Send
        PlatformType.YOUTUBE -> Icons.Default.PlayCircleFilled
        PlatformType.INSTAGRAM -> Icons.Default.PhotoCamera
        PlatformType.TWITTER -> Icons.Default.AlternateEmail
        PlatformType.COMMUNITY -> Icons.Default.Groups
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("social_card_${channel.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurfaceElevated
        ),
        border = BorderStroke(1.dp, platformColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Icon, Title, Category Badge & Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Platform Icon Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(platformColor.copy(alpha = 0.85f), platformColor)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = channel.name,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = channel.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                fontSize = 16.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (channel.isVerified) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "ভেরিফাইড পেজ",
                                tint = platformColor,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    Text(
                        text = channel.handle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GoldLight,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Favorite Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("fav_btn_${channel.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "পছন্দের তালিকায় রাখুন",
                        tint = if (isFavorite) CrimsonLight else TextSecondaryDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Followers Badge & Category Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = platformColor.copy(alpha = 0.15f),
                    border = BorderStroke(0.5.dp, platformColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = channel.followers,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = platformColor,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceCard
                ) {
                    Text(
                        text = channel.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondaryDark,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = channel.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondaryDark,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Open in App / Copy Link / Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onOpen,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .testTag("open_channel_${channel.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = platformColor,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (channel.platform) {
                            PlatformType.FACEBOOK -> "ফেসবুকে যান"
                            PlatformType.TIKTOK -> "টিকটক খুলুন"
                            PlatformType.TELEGRAM -> "টেলিগ্রামে জয়েন"
                            PlatformType.YOUTUBE -> "ইউটিউব দেখুন"
                            PlatformType.INSTAGRAM -> "ইনস্টাগ্রাম"
                            PlatformType.TWITTER -> "টুইট দেখুন"
                            PlatformType.COMMUNITY -> "ফ্যান ক্লাবে যোগ"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }

                OutlinedButton(
                    onClick = onCopy,
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("copy_link_${channel.id}"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GoldLight
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "লিংক কপি",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("কপি", style = MaterialTheme.typography.labelSmall)
                }

                FilledTonalIconButton(
                    onClick = onShare,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("share_channel_${channel.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = DarkSurfaceCard,
                        contentColor = TextPrimaryDark
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "শেয়ার করুন",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
