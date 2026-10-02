package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavTab
import com.example.ui.SkViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: SkViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsState()

                // Handle back press: if search active, close search; if not on HOME, navigate to HOME
                BackHandler(enabled = uiState.isSearchActive || uiState.currentTab != AppNavTab.HOME) {
                    if (uiState.isSearchActive) {
                        viewModel.toggleSearchActive(false)
                    } else if (uiState.currentTab != AppNavTab.HOME) {
                        viewModel.setTab(AppNavTab.HOME)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        TopAppBar(
                            title = {
                                if (uiState.isSearchActive) {
                                    OutlinedTextField(
                                        value = uiState.searchQuery,
                                        onValueChange = { viewModel.setSearchQuery(it) },
                                        placeholder = { Text("ফেসবুক, টিকটক, সিনেমা বা গান খুঁজুন...") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("search_text_field"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GoldPrimary,
                                            unfocusedBorderColor = DarkSurfaceCard,
                                            focusedTextColor = TextPrimaryDark,
                                            unfocusedTextColor = TextPrimaryDark
                                        ),
                                        trailingIcon = {
                                            if (uiState.searchQuery.isNotBlank()) {
                                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                                    Icon(Icons.Default.Clear, contentDescription = "মুছুন", tint = TextSecondaryDark)
                                                }
                                            }
                                        }
                                    )
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(GoldPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Shakib Khan Hub",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = TextPrimaryDark,
                                                    fontSize = 17.sp
                                                )
                                            )
                                            Text(
                                                text = "কিং খান শাকিব খান পোর্টাল",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = GoldLight,
                                                    fontSize = 10.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        viewModel.toggleSearchActive(!uiState.isSearchActive)
                                    },
                                    modifier = Modifier.testTag("top_search_btn")
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                        contentDescription = "সার্চ",
                                        tint = GoldLight
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.shareContent(
                                            title = "Shakib Khan Hub - কিং খান অ্যাপ",
                                            text = "শাকিব খানের অফিসিয়াল ফেসবুক, টিকটক, টেলিগ্রাম, সিনেমা ও গানের সুপার ফ্যান হাব অ্যাপ!",
                                            url = "https://www.facebook.com/shakibkhanofficial"
                                        )
                                    },
                                    modifier = Modifier.testTag("top_share_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "শেয়ার করুন",
                                        tint = GoldLight
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = DarkBackground,
                                titleContentColor = TextPrimaryDark,
                                actionIconContentColor = GoldLight
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = DarkSurfaceElevated,
                            contentColor = GoldLight,
                            tonalElevation = 6.dp
                        ) {
                            val items = listOf(
                                Triple(AppNavTab.HOME, Icons.Default.Home, Icons.Outlined.Home),
                                Triple(AppNavTab.SOCIAL, Icons.Default.Public, Icons.Outlined.Public),
                                Triple(AppNavTab.MOVIES, Icons.Default.Movie, Icons.Outlined.Movie),
                                Triple(AppNavTab.MEDIA, Icons.Default.MusicNote, Icons.Outlined.MusicNote),
                                Triple(AppNavTab.GALLERY, Icons.Default.PhotoLibrary, Icons.Outlined.PhotoLibrary),
                                Triple(AppNavTab.FAVORITES, Icons.Default.Favorite, Icons.Outlined.FavoriteBorder)
                            )

                            items.forEach { (tab, filledIcon, outlinedIcon) ->
                                val isSelected = uiState.currentTab == tab
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = {
                                        if (uiState.isSearchActive) {
                                            viewModel.toggleSearchActive(false)
                                        }
                                        viewModel.setTab(tab)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) filledIcon else outlinedIcon,
                                            contentDescription = tab.titleBangla,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.titleBangla,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = GoldPrimary,
                                        indicatorColor = GoldPrimary,
                                        unselectedIconColor = TextSecondaryDark,
                                        unselectedTextColor = TextSecondaryDark
                                    ),
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(DarkBackground)
                    ) {
                        if (uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
                            SearchResultsView(
                                query = uiState.searchQuery,
                                viewModel = viewModel
                            )
                        } else {
                            AnimatedContent(
                                targetState = uiState.currentTab,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                },
                                label = "ScreenTransition"
                            ) { tab ->
                                when (tab) {
                                    AppNavTab.HOME -> HomeScreen(viewModel = viewModel)
                                    AppNavTab.SOCIAL -> SocialHubScreen(viewModel = viewModel)
                                    AppNavTab.MOVIES -> MoviesScreen(viewModel = viewModel)
                                    AppNavTab.MEDIA -> MediaScreen(viewModel = viewModel)
                                    AppNavTab.GALLERY -> GalleryScreen(viewModel = viewModel)
                                    AppNavTab.FAVORITES -> FavoritesScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsView(
    query: String,
    viewModel: SkViewModel
) {
    val q = query.trim().lowercase()

    val matchedSocial = viewModel.allSocialChannels.filter {
        it.name.lowercase().contains(q) || it.handle.lowercase().contains(q) || it.description.lowercase().contains(q)
    }

    val matchedMovies = viewModel.allMovies.filter {
        it.titleBangla.lowercase().contains(q) || it.titleEnglish.lowercase().contains(q) || it.genre.lowercase().contains(q) || it.director.lowercase().contains(q)
    }

    val matchedSongs = viewModel.allSongs.filter {
        it.title.lowercase().contains(q) || it.movie.lowercase().contains(q) || it.singers.lowercase().contains(q)
    }

    val matchedDialogues = viewModel.allDialogues.filter {
        it.quote.lowercase().contains(q) || it.movie.lowercase().contains(q) || it.character.lowercase().contains(q)
    }

    val totalMatches = matchedSocial.size + matchedMovies.size + matchedSongs.size + matchedDialogues.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("search_results_list")
    ) {
        item {
            Text(
                text = "অনুসন্ধানের ফলাফল ($totalMatches টি পাওয়া গেছে)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (totalMatches == 0) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = TextSecondaryDark,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"$query\" এর জন্য কিছু খুঁজে পাওয়া যায়নি",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                }
            }
        }

        // Social results
        if (matchedSocial.isNotEmpty()) {
            item {
                Text(
                    text = "🌐 সোশ্যাল মিডিয়া",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = CrimsonLight),
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            }
            items(matchedSocial) { channel ->
                SearchResultItem(
                    title = channel.name,
                    subtitle = "${channel.handle} • ${channel.followers}",
                    tag = channel.category,
                    onClick = { viewModel.openUrl(channel.webUrl, channel.appIntentUri) }
                )
            }
        }

        // Movies results
        if (matchedMovies.isNotEmpty()) {
            item {
                Text(
                    text = "🎬 সিনেমা",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = CrimsonLight),
                    modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
                )
            }
            items(matchedMovies) { movie ->
                SearchResultItem(
                    title = movie.titleBangla,
                    subtitle = "${movie.year} • ${movie.genre} • পরিচালক: ${movie.director}",
                    tag = movie.rating,
                    onClick = { viewModel.selectMovie(movie) }
                )
            }
        }

        // Songs results
        if (matchedSongs.isNotEmpty()) {
            item {
                Text(
                    text = "🎵 গান",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = CrimsonLight),
                    modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
                )
            }
            items(matchedSongs) { song ->
                SearchResultItem(
                    title = song.title,
                    subtitle = "${song.movie} • ${song.singers}",
                    tag = song.viewsCount,
                    onClick = { viewModel.openUrl(song.youtubeUrl) }
                )
            }
        }

        // Dialogues results
        if (matchedDialogues.isNotEmpty()) {
            item {
                Text(
                    text = "💬 ডায়লগ",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = CrimsonLight),
                    modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
                )
            }
            items(matchedDialogues) { dlg ->
                SearchResultItem(
                    title = "\"${dlg.quote}\"",
                    subtitle = "${dlg.movie} • চরিত্র: ${dlg.character}",
                    tag = "ডায়লগ",
                    onClick = { viewModel.copyToClipboard(dlg.quote, "ডায়লগ") }
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    title: String,
    subtitle: String,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(0.5.dp, DarkSurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = DarkSurfaceCard
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GoldLight,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}
