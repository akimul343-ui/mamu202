package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.FavoriteEntity
import com.example.data.database.FanWishEntity
import com.example.data.models.Dialogue
import com.example.data.models.GalleryItem
import com.example.data.models.Movie
import com.example.data.models.NewsItem
import com.example.data.models.SocialChannel
import com.example.data.models.Song
import com.example.data.repository.SkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab(val titleBangla: String) {
    HOME("হোম"),
    SOCIAL("সোশ্যাল হাব"),
    MOVIES("সিনেমা"),
    MEDIA("গান ও ডায়লগ"),
    GALLERY("গ্যালারি"),
    FAVORITES("পছন্দ")
}

data class SkUiState(
    val currentTab: AppNavTab = AppNavTab.HOME,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedMovieCategory: String = "সব",
    val selectedMovie: Movie? = null,
    val selectedGalleryItem: GalleryItem? = null,
    val isAddWishDialogOpen: Boolean = false
)

class SkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkRepository
    private val _uiState = MutableStateFlow(SkUiState())
    val uiState: StateFlow<SkUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = SkRepository(db.favoriteDao())
    }

    val favorites: StateFlow<List<FavoriteEntity>> = repository.favorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val fanWishes: StateFlow<List<FanWishEntity>> = repository.fanWishes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allSocialChannels = repository.getSocialChannels()
    val allMovies = repository.getMovies()
    val allSongs = repository.getHitSongs()
    val allDialogues = repository.getFamousDialogues()
    val allNews = repository.getLatestNews()
    val allGallery = repository.getGalleryItems()

    fun setTab(tab: AppNavTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleSearchActive(active: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSearchActive = active,
            searchQuery = if (!active) "" else _uiState.value.searchQuery
        )
    }

    fun setMovieCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedMovieCategory = category)
    }

    fun selectMovie(movie: Movie?) {
        _uiState.value = _uiState.value.copy(selectedMovie = movie)
    }

    fun selectGalleryItem(item: GalleryItem?) {
        _uiState.value = _uiState.value.copy(selectedGalleryItem = item)
    }

    fun toggleAddWishDialog(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAddWishDialogOpen = open)
    }

    fun isItemFavorite(id: String): Boolean {
        return favorites.value.any { it.id == id }
    }

    fun toggleMovieFavorite(movie: Movie) {
        viewModelScope.launch {
            val isFav = isItemFavorite(movie.id)
            repository.toggleFavorite(
                FavoriteEntity(
                    id = movie.id,
                    type = "MOVIE",
                    title = movie.titleBangla,
                    subtitle = "${movie.year} • ${movie.genre}",
                    actionUrl = movie.trailerUrl
                ),
                isFav
            )
            val msg = if (isFav) "পছন্দের তালিকা থেকে সরানো হয়েছে" else "পছন্দের তালিকায় যুক্ত হয়েছে!"
            showToast(msg)
        }
    }

    fun toggleSongFavorite(song: Song) {
        viewModelScope.launch {
            val isFav = isItemFavorite(song.id)
            repository.toggleFavorite(
                FavoriteEntity(
                    id = song.id,
                    type = "SONG",
                    title = song.title,
                    subtitle = "${song.movie} • ${song.singers}",
                    actionUrl = song.youtubeUrl
                ),
                isFav
            )
            val msg = if (isFav) "গানটি পছন্দ থেকে সরানো হয়েছে" else "গানটি পছন্দের তালিকায় যুক্ত হয়েছে!"
            showToast(msg)
        }
    }

    fun toggleDialogueFavorite(dialogue: Dialogue) {
        viewModelScope.launch {
            val isFav = isItemFavorite(dialogue.id)
            repository.toggleFavorite(
                FavoriteEntity(
                    id = dialogue.id,
                    type = "DIALOGUE",
                    title = dialogue.quote,
                    subtitle = "${dialogue.movie} • চরিত্র: ${dialogue.character}",
                    actionUrl = ""
                ),
                isFav
            )
            val msg = if (isFav) "ডায়লগ সরানো হয়েছে" else "ডায়লগ পছন্দের তালিকায় যুক্ত হয়েছে!"
            showToast(msg)
        }
    }

    fun toggleSocialFavorite(channel: SocialChannel) {
        viewModelScope.launch {
            val isFav = isItemFavorite(channel.id)
            repository.toggleFavorite(
                FavoriteEntity(
                    id = channel.id,
                    type = "SOCIAL",
                    title = channel.name,
                    subtitle = channel.followers,
                    actionUrl = channel.webUrl
                ),
                isFav
            )
            val msg = if (isFav) "চ্যানেল পছন্দের তালিকা থেকে সরানো হয়েছে" else "চ্যানেল প্রিয় তালিকায় যুক্ত হয়েছে!"
            showToast(msg)
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            val fav = favorites.value.find { it.id == id }
            if (fav != null) {
                repository.toggleFavorite(fav, true)
                showToast("পছন্দ থেকে সরানো হয়েছে")
            }
        }
    }

    fun submitFanWish(author: String, message: String, location: String) {
        if (message.isBlank()) {
            showToast("অনুগ্রহ করে আপনার বার্তা লিখুন!")
            return
        }
        viewModelScope.launch {
            repository.addFanWish(author, message, location)
            toggleAddWishDialog(false)
            showToast("আপনার বার্তা কিং খানের ফ্যান ওয়ালে পোস্ট হয়েছে! 🌟")
        }
    }

    fun deleteFanWish(id: Int) {
        viewModelScope.launch {
            repository.deleteFanWish(id)
            showToast("বার্তা মুছে ফেলা হয়েছে")
        }
    }

    fun copyToClipboard(text: String, label: String = "কপি করা হয়েছে") {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        showToast("$label ক্লিপবোর্ডে কপি করা হয়েছে! 📋")
    }

    fun openUrl(url: String, appIntentUri: String? = null) {
        val context = getApplication<Application>()
        var launched = false

        if (!appIntentUri.isNullOrBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appIntentUri)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                launched = true
            } catch (e: Exception) {
                launched = false
            }
        }

        if (!launched) {
            try {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            } catch (e: Exception) {
                showToast("লিংক খুলতে সমস্যা হয়েছে: $url")
            }
        }
    }

    fun shareContent(title: String, text: String, url: String = "") {
        val context = getApplication<Application>()
        try {
            val shareText = if (url.isNotBlank()) "$title\n\n$text\n\nলিংক: $url\n\n[কিং খান শাকিব খান হাব অ্যাপ]" else "$title\n\n$text\n\n[কিং খান শাকিব খান হাব অ্যাপ]"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val shareIntent = Intent.createChooser(sendIntent, "শেয়ার করুন:").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            showToast("শেয়ার করা সম্ভব হয়নি")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}
