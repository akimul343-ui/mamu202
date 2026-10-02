package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.database.FavoriteEntity
import com.example.data.database.FanWishEntity
import com.example.ui.SkViewModel
import com.example.ui.theme.*

@Composable
fun FavoritesScreen(
    viewModel: SkViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()
    val fanWishes by viewModel.fanWishes.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Favorites, 1: Fan Wish Wall

    Box(modifier = modifier.fillMaxSize().testTag("favorites_screen")) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "পছন্দ ও ফ্যান বার্তা ওয়াল",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimaryDark,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = "আপনার সেভ করা কন্টেন্ট এবং কিং খানের উদ্দেশ্যে ভক্তদের বার্তা",
                    style = MaterialTheme.typography.bodySmall.copy(color = GoldLight)
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurfaceElevated,
                    contentColor = GoldPrimary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "⭐ প্রিয় তালিকা (${favorites.size})",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 0) GoldLight else TextSecondaryDark
                                )
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "💌 ফ্যান ওয়াল (${fanWishes.size})",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 1) GoldLight else TextSecondaryDark
                                )
                            )
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                // Favorites List
                if (favorites.isEmpty()) {
                    EmptyStateView(
                        icon = Icons.Default.FavoriteBorder,
                        title = "কোনো পছন্দের আইটেম নেই",
                        subtitle = "সিনেমা, গান বা ডায়লগ কার্ডের হার্ট আইকনে ট্যাপ করে এখানে সেভ করে রাখতে পারেন।"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(favorites) { item ->
                            FavoriteItemCard(
                                item = item,
                                onOpen = {
                                    if (item.actionUrl.isNotBlank()) {
                                        viewModel.openUrl(item.actionUrl)
                                    }
                                },
                                onRemove = { viewModel.removeFavorite(item.id) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            } else {
                // Fan Wish Wall
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "শাকিবিয়ান ফ্যান ওয়াল",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                        Button(
                            onClick = { viewModel.toggleAddWishDialog(true) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            modifier = Modifier.testTag("add_wish_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("বার্তা লিখুন", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    if (fanWishes.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.MarkChatRead,
                            title = "এখনো কোনো ফ্যান বার্তা নেই",
                            subtitle = "'বার্তা লিখুন' বাটনে চাপ দিয়ে কিং খানের উদ্দেশ্যে আপনার শুভকামনা বার্তা পোস্ট করুন!"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(fanWishes) { wish ->
                                FanWishCard(
                                    wish = wish,
                                    onDelete = { viewModel.deleteFanWish(wish.id) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        // Add Fan Wish Dialog
        if (uiState.isAddWishDialogOpen) {
            AddWishDialog(
                onDismiss = { viewModel.toggleAddWishDialog(false) },
                onSubmit = { name, message, location ->
                    viewModel.submitFanWish(name, message, location)
                }
            )
        }
    }
}

@Composable
fun FavoriteItemCard(
    item: FavoriteEntity,
    onOpen: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("fav_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (item.type) {
                    "MOVIE" -> CrimsonAccent
                    "SONG" -> GoldPrimary
                    "DIALOGUE" -> TelegramBlue
                    else -> FacebookBlue
                }
            ) {
                Text(
                    text = when (item.type) {
                        "MOVIE" -> "সিনেমা"
                        "SONG" -> "গান"
                        "DIALOGUE" -> "ডায়লগ"
                        else -> "সোশ্যাল"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (item.type == "SONG") Color.Black else Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.subtitle.isNotBlank()) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (item.actionUrl.isNotBlank()) {
                IconButton(onClick = onOpen) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "খুলুন",
                        tint = GoldLight
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "মুছে ফেলুন",
                    tint = TextSecondaryDark
                )
            }
        }
    }
}

@Composable
fun FanWishCard(
    wish: FanWishEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("wish_card_${wish.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = wish.author,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${wish.location}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "ডিলিট",
                        tint = TextSecondaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = wish.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimaryDark,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Composable
fun AddWishDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = BorderStroke(1.dp, GoldPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "👑 কিং খানের জন্য বার্তা লিখুন",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("আপনার নাম") },
                    placeholder = { Text("যেমন: আকাশ আহমেদ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_wish_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkSurfaceCard,
                        focusedLabelColor = GoldLight
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("আপনার এলাকা / জেলা") },
                    placeholder = { Text("যেমন: ঢাকা, বাংলাদেশ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_wish_location"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkSurfaceCard,
                        focusedLabelColor = GoldLight
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("আপনার শুভকামনা বার্তা") },
                    placeholder = { Text("কিং খানের জন্য আপনার ভালোবাসা প্রকাশ করুন...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("input_wish_message"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkSurfaceCard,
                        focusedLabelColor = GoldLight
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("বাতিল", color = TextSecondaryDark)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSubmit(name, message, location) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier.testTag("submit_wish_btn")
                    ) {
                        Text("পোস্ট করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GoldLight.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondaryDark,
                lineHeight = 18.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
