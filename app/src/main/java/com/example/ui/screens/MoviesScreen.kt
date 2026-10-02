package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.models.Movie
import com.example.ui.SkViewModel
import com.example.ui.components.MovieCard
import com.example.ui.theme.*

@Composable
fun MoviesScreen(
    viewModel: SkViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = listOf("সব", "ব্লকবাস্টার", "আসন্ন", "অ্যাকশন", "রোমান্টিক")

    val filteredMovies = viewModel.allMovies.filter { movie ->
        when (uiState.selectedMovieCategory) {
            "সব" -> true
            "ব্লকবাস্টার" -> movie.isBlockbuster
            "আসন্ন" -> movie.year.contains("আসন্ন")
            "অ্যাকশন" -> movie.genre.contains("অ্যাকশন")
            "রোমান্টিক" -> movie.genre.contains("রোমান্টিক")
            else -> true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("movies_screen"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "শাকিব খানের ব্লকবাস্টার সিনেমাসমূহ",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark,
                            fontSize = 22.sp
                        )
                    )
                    Text(
                        text = "তুফান, প্রিয়তমা, রাজকুমার, বরবাদ সহ সেরা সিনেমা ও অফিসিয়াল ট্রেইলার",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GoldLight
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Categories Row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = uiState.selectedMovieCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setMovieCategory(cat) },
                                label = { Text(cat) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonAccent,
                                    selectedLabelColor = Color.White,
                                    containerColor = DarkSurfaceElevated,
                                    labelColor = TextPrimaryDark
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) CrimsonAccent else DarkSurfaceCard,
                                    selectedBorderColor = CrimsonAccent
                                )
                            )
                        }
                    }
                }
            }

            // Movie items
            items(filteredMovies) { movie ->
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
        }

        // Movie Detail Modal Dialog
        uiState.selectedMovie?.let { movie ->
            MovieDetailDialog(
                movie = movie,
                isFavorite = viewModel.isItemFavorite(movie.id),
                onDismiss = { viewModel.selectMovie(null) },
                onWatchTrailer = { viewModel.openUrl(movie.trailerUrl) },
                onToggleFavorite = { viewModel.toggleMovieFavorite(movie) },
                onShare = {
                    viewModel.shareContent(
                        title = movie.titleBangla,
                        text = "${movie.titleBangla} (${movie.year})\n${movie.synopsis}\n\nডায়লগ: \"${movie.iconicDialogue}\"",
                        url = movie.trailerUrl
                    )
                }
            )
        }
    }
}

@Composable
fun MovieDetailDialog(
    movie: Movie,
    isFavorite: Boolean,
    onDismiss: () -> Unit,
    onWatchTrailer: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val drawableId = movie.posterDrawable ?: R.drawable.sk_hero_banner
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = movie.titleBangla,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movie.titleBangla,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimaryDark,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "${movie.year} • ${movie.genre}",
                            style = MaterialTheme.typography.bodySmall.copy(color = GoldLight)
                        )
                    }

                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) CrimsonLight else TextSecondaryDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "পরিচালক: ${movie.director} | কাস্ট: ${movie.coStar}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceCard
                ) {
                    Text(
                        text = "🏆 ${movie.boxOffice}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = movie.synopsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimaryDark.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkBackground,
                    border = BorderStroke(0.5.dp, GoldPrimary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "🔥 সেরা ডায়লগ: \"${movie.iconicDialogue}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GoldLight,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onWatchTrailer,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonAccent)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ইউটিউবে ট্রেইলার", style = MaterialTheme.typography.labelMedium)
                    }

                    OutlinedButton(
                        onClick = onShare,
                        modifier = Modifier.height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
