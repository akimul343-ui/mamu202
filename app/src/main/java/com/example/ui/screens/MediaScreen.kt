package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SkViewModel
import com.example.ui.components.DialogueCard
import com.example.ui.components.SongCard
import com.example.ui.theme.*

@Composable
fun MediaScreen(
    viewModel: SkViewModel,
    modifier: Modifier = Modifier
) {
    var selectedMediaTab by remember { mutableStateOf(0) } // 0: Songs, 1: Dialogues

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("media_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "গান ও জনপ্রিয় ডায়লগ",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimaryDark,
                    fontSize = 22.sp
                )
            )
            Text(
                text = "লাগে উরাধুড়া, ঈশ্বর, প্রিয়তমা গান এবং সেরা সব পাঞ্চ ডায়লগ কালেকশন",
                style = MaterialTheme.typography.bodySmall.copy(color = GoldLight)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tab selector
            TabRow(
                selectedTabIndex = selectedMediaTab,
                containerColor = DarkSurfaceElevated,
                contentColor = GoldPrimary
            ) {
                Tab(
                    selected = selectedMediaTab == 0,
                    onClick = { selectedMediaTab = 0 },
                    text = {
                        Text(
                            text = "🎵 হিট গান (${viewModel.allSongs.size})",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedMediaTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedMediaTab == 0) GoldLight else TextSecondaryDark
                            )
                        )
                    }
                )
                Tab(
                    selected = selectedMediaTab == 1,
                    onClick = { selectedMediaTab = 1 },
                    text = {
                        Text(
                            text = "💬 সেরা ডায়লগ (${viewModel.allDialogues.size})",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedMediaTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedMediaTab == 1) GoldLight else TextSecondaryDark
                            )
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (selectedMediaTab == 0) {
                items(viewModel.allSongs) { song ->
                    val isFav = viewModel.isItemFavorite(song.id)
                    SongCard(
                        song = song,
                        isFavorite = isFav,
                        onPlay = { viewModel.openUrl(song.youtubeUrl) },
                        onCopyLyrics = { viewModel.copyToClipboard(song.lyricsSnippet, "গানের লিরিক্স") },
                        onToggleFavorite = { viewModel.toggleSongFavorite(song) },
                        onShare = {
                            viewModel.shareContent(
                                title = song.title,
                                text = "শাকিব খানের গান: ${song.title} (${song.movie})\nশিল্পী: ${song.singers}\n\nলিরিক্স: \"${song.lyricsSnippet}\"",
                                url = song.youtubeUrl
                            )
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            } else {
                items(viewModel.allDialogues) { dialogue ->
                    val isFav = viewModel.isItemFavorite(dialogue.id)
                    DialogueCard(
                        dialogue = dialogue,
                        isFavorite = isFav,
                        onCopy = { viewModel.copyToClipboard(dialogue.quote, "ডায়লগ") },
                        onShare = {
                            viewModel.shareContent(
                                title = "শাকিব খানের ডায়লগ",
                                text = "\"${dialogue.quote}\"\n\nমুভি: ${dialogue.movie} • চরিত্র: ${dialogue.character}"
                            )
                        },
                        onToggleFavorite = { viewModel.toggleDialogueFavorite(dialogue) },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}
