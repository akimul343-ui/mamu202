package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.AppNavTab
import com.example.ui.SkViewModel
import com.example.ui.components.DialogueCard
import com.example.ui.components.MovieCard
import com.example.ui.components.NewsCard
import com.example.ui.components.SongCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: SkViewModel,
    modifier: Modifier = Modifier
) {
    val movies = viewModel.allMovies.take(3)
    val news = viewModel.allNews.take(2)
    val topSong = viewModel.allSongs.firstOrNull()
    val topDialogue = viewModel.allDialogues.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sk_hero_banner),
                    contentDescription = "কিং খান শাকিব খান",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dramatic Dark & Gold gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.6f),
                                    DarkBackground
                                )
                            )
                        )
                )

                // Banner Content
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CrimsonAccent,
                        border = BorderStroke(1.dp, GoldPrimary)
                    ) {
                        Text(
                            text = "👑 ঢালিউড মেগাস্টার হাব",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "কিং খান শাকিব খান",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark,
                            fontSize = 24.sp
                        )
                    )

                    Text(
                        text = "ফেসবুক • টিকটক • টেলিগ্রাম • সিনেমা ও গানের অফিশিয়াল হাব",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GoldLight,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        // Quick Launch Social Bar (Directly answering user's prompt: Facebook, TikTok, Telegram, YouTube)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "সোশ্যাল মিডিয়া কুইক লিংক",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 17.sp
                        )
                    )
                    TextButton(
                        onClick = { viewModel.setTab(AppNavTab.SOCIAL) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "সবগুলো দেখুন →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 4 prominent quick launcher tiles: Facebook, TikTok, Telegram, YouTube
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickSocialButton(
                        name = "ফেসবুক",
                        icon = Icons.Default.Public,
                        bgColor = FacebookBlue,
                        badge = "৬.২M",
                        onClick = {
                            viewModel.openUrl(
                                "https://www.facebook.com/shakibkhanofficial",
                                "fb://facewebmodal/f?href=https://www.facebook.com/shakibkhanofficial"
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuickSocialButton(
                        name = "টিকটক",
                        icon = Icons.Default.MusicVideo,
                        bgColor = TikTokPink,
                        badge = "৩.৮M",
                        onClick = {
                            viewModel.openUrl(
                                "https://www.tiktok.com/@shakibkhan_official",
                                "snssdk1233://user/profile/shakibkhan_official"
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuickSocialButton(
                        name = "টেলিগ্রাম",
                        icon = Icons.Default.Send,
                        bgColor = TelegramBlue,
                        badge = "১.৮L",
                        onClick = {
                            viewModel.openUrl(
                                "https://t.me/shakibkhanhub",
                                "tg://resolve?domain=shakibkhanhub"
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuickSocialButton(
                        name = "ইউটিউব",
                        icon = Icons.Default.PlayCircleFilled,
                        bgColor = YouTubeRed,
                        badge = "২.৫M",
                        onClick = {
                            viewModel.openUrl(
                                "https://www.youtube.com/@SKFilmsOfficial",
                                "vnd.youtube://www.youtube.com/@SKFilmsOfficial"
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Announcement / News Ticker
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkSurfaceElevated,
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🔥 তুফান ও বরবাদের মেগা আপডেট",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            )
                            Text(
                                text = "টিকটকে লাগে উরাধুড়া ট্রেন্ডে কোটি ভিউ এবং আসন্ন বরবাদ সিনেমার শুটিং আপডেট দেখতে সোশ্যাল হাবে যান!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Trending Movies Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ব্লকবাস্টার সিনেমাসমূহ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 17.sp
                        )
                    )
                    TextButton(
                        onClick = { viewModel.setTab(AppNavTab.MOVIES) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "সব সিনেমা →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        items(movies) { movie ->
            val isFav = viewModel.isItemFavorite(movie.id)
            MovieCard(
                movie = movie,
                isFavorite = isFav,
                onMovieClick = { viewModel.selectMovie(movie) },
                onWatchTrailer = { viewModel.openUrl(movie.trailerUrl) },
                onToggleFavorite = { viewModel.toggleMovieFavorite(movie) },
                onShare = {
                    viewModel.shareContent(
                        title = movie.titleBangla,
                        text = "${movie.titleBangla} (${movie.year})\nপরিচালক: ${movie.director}\nবক্স অফিস: ${movie.boxOffice}",
                        url = movie.trailerUrl
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Top Hit Song of the week
        if (topSong != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সপ্তাহের শীর্ষ হিট গান",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                fontSize = 17.sp
                            )
                        )
                        TextButton(
                            onClick = { viewModel.setTab(AppNavTab.MEDIA) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "সব গান →",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    val isFav = viewModel.isItemFavorite(topSong.id)
                    SongCard(
                        song = topSong,
                        isFavorite = isFav,
                        onPlay = { viewModel.openUrl(topSong.youtubeUrl) },
                        onCopyLyrics = { viewModel.copyToClipboard(topSong.lyricsSnippet, "গানের লিরিক্স") },
                        onToggleFavorite = { viewModel.toggleSongFavorite(topSong) },
                        onShare = {
                            viewModel.shareContent(
                                title = topSong.title,
                                text = "কিং খানের সেরা গান: ${topSong.title} (${topSong.movie})",
                                url = topSong.youtubeUrl
                            )
                        }
                    )
                }
            }
        }

        // Iconic Dialogue Spotlight
        if (topDialogue != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "আজকের সেরা ডায়লগ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 17.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val isFav = viewModel.isItemFavorite(topDialogue.id)
                    DialogueCard(
                        dialogue = topDialogue,
                        isFavorite = isFav,
                        onCopy = { viewModel.copyToClipboard(topDialogue.quote, "ডায়লগ") },
                        onShare = {
                            viewModel.shareContent(
                                title = "শাকিব খানের ডায়লগ",
                                text = "\"${topDialogue.quote}\" - ${topDialogue.movie} (চরিত্র: ${topDialogue.character})"
                            )
                        },
                        onToggleFavorite = { viewModel.toggleDialogueFavorite(topDialogue) }
                    )
                }
            }
        }

        // Latest Updates & News
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "সর্বশেষ তাজা খবর",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontSize = 17.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                news.forEach { item ->
                    NewsCard(
                        news = item,
                        onReadMore = { viewModel.openUrl(item.url) },
                        onShare = {
                            viewModel.shareContent(
                                title = item.title,
                                text = "${item.title}\n\n${item.summary}",
                                url = item.url
                            )
                        },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickSocialButton(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("quick_${name}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, bgColor.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark,
                    fontSize = 12.sp
                )
            )

            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = bgColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp
                )
            )
        }
    }
}
