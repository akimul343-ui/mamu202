package com.example.data.repository

import com.example.R
import com.example.data.database.FavoriteDao
import com.example.data.database.FavoriteEntity
import com.example.data.database.FanWishEntity
import com.example.data.models.Dialogue
import com.example.data.models.GalleryItem
import com.example.data.models.Movie
import com.example.data.models.NewsItem
import com.example.data.models.PlatformType
import com.example.data.models.SocialChannel
import com.example.data.models.Song
import kotlinx.coroutines.flow.Flow

class SkRepository(private val dao: FavoriteDao) {

    val favorites: Flow<List<FavoriteEntity>> = dao.getAllFavorites()
    val fanWishes: Flow<List<FanWishEntity>> = dao.getAllFanWishes()

    fun isFavorite(id: String): Flow<Boolean> = dao.isFavorite(id)

    suspend fun toggleFavorite(entity: FavoriteEntity, isFav: Boolean) {
        if (isFav) {
            dao.deleteFavoriteById(entity.id)
        } else {
            dao.insertFavorite(entity)
        }
    }

    suspend fun addFanWish(author: String, message: String, location: String) {
        dao.insertFanWish(
            FanWishEntity(
                author = author.ifBlank { "শাকিবিয়ান ফ্যান" },
                message = message,
                location = location.ifBlank { "বাংলাদেশ" }
            )
        )
    }

    suspend fun deleteFanWish(id: Int) {
        dao.deleteFanWish(id)
    }

    // Official and Fan Social Channels
    fun getSocialChannels(): List<SocialChannel> = listOf(
        SocialChannel(
            id = "fb_official",
            platform = PlatformType.FACEBOOK,
            name = "Shakib Khan (অফিসিয়াল)",
            handle = "@shakibkhanofficial",
            followers = "৬.২ মিলিয়ন+ ফলোয়ার্স",
            description = "শাকিব খানের একমাত্র অফিসিয়াল ভেরিফাইড ফেসবুক পেজ। নতুন সিনেমার ফার্স্ট লুক ও লাইভ আপডেট সরাসরি পেতে ফলো করুন।",
            webUrl = "https://www.facebook.com/shakibkhanofficial",
            appIntentUri = "fb://facewebmodal/f?href=https://www.facebook.com/shakibkhanofficial",
            isVerified = true,
            badgeColor = 0xFF1877F2,
            category = "ফেসবুক"
        ),
        SocialChannel(
            id = "tiktok_official",
            platform = PlatformType.TIKTOK,
            name = "Shakib Khan (টিকটক)",
            handle = "@shakibkhan_official",
            followers = "৩.৮ মিলিয়ন+ ফলোয়ার্স",
            description = "কিং খানের অফিশিয়াল টিকটক একাউন্ট। 'তুফান' ও 'প্রিয়তমা' গানের ভাইরাল ক্লিপস, ট্রেন্ডিং রিলস ও শুটিং বিটিএস দেখুন।",
            webUrl = "https://www.tiktok.com/@shakibkhan_official",
            appIntentUri = "snssdk1233://user/profile/shakibkhan_official",
            isVerified = true,
            badgeColor = 0xFFFE2C55,
            category = "টিকটক"
        ),
        SocialChannel(
            id = "tiktok_trending",
            platform = PlatformType.TIKTOK,
            name = "TikTok Trending #ShakibKhan",
            handle = "#ShakibKhan #Toofan",
            followers = "১.৫ বিলিয়ন+ ভিউজ",
            description = "টিকটকে শাকিব খানের সব ট্রেন্ডিং ভিডিও, ডায়লগ মিম এবং তুফান-প্রিয়তমার ভাইরাল ক্লিপ দেখুন।",
            webUrl = "https://www.tiktok.com/tag/shakibkhan",
            appIntentUri = "snssdk1233://challenge/detail/shakibkhan",
            isVerified = false,
            badgeColor = 0xFF25F4EE,
            category = "টিকটক ট্রেন্ডিং"
        ),
        SocialChannel(
            id = "telegram_channel",
            platform = PlatformType.TELEGRAM,
            name = "Shakibian Mega Telegram",
            handle = "@ShakibKhanOfficialFans",
            followers = "১ লাখ ৮০ হাজার+ মেম্বার",
            description = "টেলিগ্রাম অফিসিয়াল ফ্যান গ্রুপ ও চ্যানেল। তুফান, বরবাদ মুভির এক্সক্লুসিভ আপডেট, এইচডি পিকচার ও টিকিট লিংক সবার আগে পেতে জয়েন হন।",
            webUrl = "https://t.me/shakibkhanhub",
            appIntentUri = "tg://resolve?domain=shakibkhanhub",
            isVerified = true,
            badgeColor = 0xFF229ED9,
            category = "টেলিগ্রাম"
        ),
        SocialChannel(
            id = "telegram_chat",
            platform = PlatformType.TELEGRAM,
            name = "Shakib Khan Fan Chat Group",
            handle = "@SKFansChatGroup",
            followers = "৫২ হাজার+ মেম্বার",
            description = "সারা বিশ্বের শাকিব ভক্তদের জন্য টেলিগ্রাম আড্ডা ও ডিসকাশন গ্রুপ। সিনেমা নিয়ে মতামত দিন ও নতুন আপডেট জানুন।",
            webUrl = "https://t.me/skfanschat",
            appIntentUri = "tg://resolve?domain=skfanschat",
            isVerified = false,
            badgeColor = 0xFF0088CC,
            category = "টেলিগ্রাম চ্যাট"
        ),
        SocialChannel(
            id = "yt_official",
            platform = PlatformType.YOUTUBE,
            name = "Shakib Khan (ইউটিউব)",
            handle = "@SKFilmsOfficial",
            followers = "২.৫ মিলিয়ন+ সাবস্ক্রাইবার",
            description = "শাকিব খানের এসকে ফিল্মস (SK Films) অফিসিয়াল ইউটিউব চ্যানেল। অফিসিয়াল ট্রেইলার, টিজার ও গান সরাসরি দেখুন।",
            webUrl = "https://www.youtube.com/@SKFilmsOfficial",
            appIntentUri = "vnd.youtube://www.youtube.com/@SKFilmsOfficial",
            isVerified = true,
            badgeColor = 0xFFFF0000,
            category = "ইউটিউব"
        ),
        SocialChannel(
            id = "instagram_official",
            platform = PlatformType.INSTAGRAM,
            name = "Shakib Khan (ইনস্টাগ্রাম)",
            handle = "@iamshakibkhan",
            followers = "২.২ মিলিয়ন+ ফলোয়ার্স",
            description = "শাকিব খানের অফিসিয়াল ইনস্টাগ্রাম আইডি। এক্সক্লুসিভ স্টাইলিশ ফটোশুট ও পার্সোনাল মোমেন্টসের ছবি দেখতে ফলো করুন।",
            webUrl = "https://www.instagram.com/iamshakibkhan",
            appIntentUri = "instagram://user?username=iamshakibkhan",
            isVerified = true,
            badgeColor = 0xFFE1306C,
            category = "ইনস্টাগ্রাম"
        ),
        SocialChannel(
            id = "twitter_official",
            platform = PlatformType.TWITTER,
            name = "Shakib Khan on X (টুইটার)",
            handle = "@IamShakibKhan",
            followers = "৫ লাখ+ ফলোয়ার্স",
            description = "শাকিব খানের অফিসিয়াল এক্স (টুইটার) হ্যান্ডেল। আন্তর্জাতিক প্রচারণা ও গুরুত্বপূর্ণ প্রেস রিলিজ দেখতে ফলো করুন।",
            webUrl = "https://twitter.com/IamShakibKhan",
            appIntentUri = "twitter://user?screen_name=IamShakibKhan",
            isVerified = true,
            badgeColor = 0xFF1DA1F2,
            category = "টুইটার"
        ),
        SocialChannel(
            id = "fb_fan_club",
            platform = PlatformType.COMMUNITY,
            name = "Shakibian Universe (ফ্যান ক্লাব)",
            handle = "@ShakibianUniverse",
            followers = "৮ লাখ ৫০ হাজার+ সদস্য",
            description = "বৃহত্তম শাকিবিয়ান ফ্যান কমিউনিটি গ্রুপ। সারা দেশের শাকিব ভক্তদের রক্তদান কর্মসূচি, সিনেমা প্রচার ও চ্যারিটি কার্যক্রম।",
            webUrl = "https://www.facebook.com/groups/shakibianuniverse",
            appIntentUri = "fb://group/shakibianuniverse",
            isVerified = false,
            badgeColor = 0xFFFFB800,
            category = "ফ্যান ক্লাব"
        )
    )

    // Blockbuster Movies
    fun getMovies(): List<Movie> = listOf(
        Movie(
            id = "movie_toofan",
            titleBangla = "তুফান (Toofan)",
            titleEnglish = "Toofan",
            year = "২০২৪",
            director = "রায়হান রাফী",
            coStar = "মিমি চক্রবর্তী, নাবিলা, চঞ্চল চৌধুরী",
            genre = "অ্যাকশন ক্রাইম থ্রিলার",
            boxOffice = "সর্বকালের সর্বশ্রেষ্ঠ ঢালিউড ইন্ডাস্ট্রি হিট (৬৫+ কোটি)",
            rating = "৯.৪/১০",
            synopsis = "নব্বই দশকের গ্যাংস্টার সাম্রাজ্য নিয়ে নির্মিত ধামাকা অ্যাকশন সিনেমা। গালিবের তুফান রূপ ও চরম উত্তেজনাপূর্ণ গল্পের এক মাস্টারপিস।",
            iconicDialogue = "আমি কোনো সাধারণ মানুষ না, আমি তুফান!",
            trailerUrl = "https://www.youtube.com/results?search_query=Toofan+Official+Trailer+Shakib+Khan",
            posterDrawable = R.drawable.sk_toofan_poster,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_priyotoma",
            titleBangla = "প্রিয়তমা (Priyotoma)",
            titleEnglish = "Priyotoma",
            year = "২০২৩",
            director = "হিমেল আশরাফ",
            coStar = "ইধিকা পাল, শহীদুজ্জামান সেলিম",
            genre = "রোমান্টিক ট্র্যাজেডি",
            boxOffice = "ঐতিহাসিক অল-টাইম মেগা ব্লকবাস্টার (৪২+ কোটি)",
            rating = "৯.২/১০",
            synopsis = "এক অমর ভালোবাসার ট্র্যাজিক গল্প। বয়োবৃদ্ধ লুকে শাকিব খানের অভিনয় পুরো বাংলা সিনেমা জগতকে তাক লাগিয়ে দেয়।",
            iconicDialogue = "ভালোবাসা যখন নিঃস্বার্থ হয়, তখন ঈশ্বরও মাথা নত করতে বাধ্য হয়!",
            trailerUrl = "https://www.youtube.com/results?search_query=Priyotoma+Official+Trailer+Shakib+Khan",
            posterDrawable = R.drawable.sk_hero_banner,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_rajkumar",
            titleBangla = "রাজকুমার (Rajkumar)",
            titleEnglish = "Rajkumar",
            year = "২০২৪",
            director = "হিমেল আশরাফ",
            coStar = "কোর্টনি কফি (USA), তারিক আনাম খান",
            genre = "পারিবারিক রোমান্টিক ড্রামা",
            boxOffice = "সুপারহিট ব্লকবাস্টার",
            rating = "৮.৮/১০",
            synopsis = "গ্রামের ছেলে শামসুর আমেরিকা যাত্রা ও তার বাবার খোঁজে এক মানবিক আবেগময় যাত্রা। হৃদয়স্পর্শী গল্প ও সুরেলা গান।",
            iconicDialogue = "বাবা মানে মাথার ওপর ছাদ, বাবা মানে পরম আশ্রয়!",
            trailerUrl = "https://www.youtube.com/results?search_query=Rajkumar+Official+Trailer+Shakib+Khan",
            posterDrawable = R.drawable.sk_hero_banner,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_borbaad",
            titleBangla = "বরবাদ (Borbaad)",
            titleEnglish = "Borbaad",
            year = "২০২৫ (আসন্ন)",
            director = "মেহেদী হাসান হৃদয়",
            coStar = "ইধিকা পাল, যীশু সেনগুপ্ত",
            genre = "হাই-অকটেন ভায়োলেন্ট অ্যাকশন",
            boxOffice = "প্রত্যাশিত মেগা ধামাকা",
            rating = "অপেক্ষমাণ",
            synopsis = "শাকিব খানের ক্যারিয়ারের সবচেয়ে ব্যয়বহুল এবং হিংস্র অ্যাকশন অবতার। মুম্বাই ও হায়দ্রাবাদে ধারণকৃত আন্তর্জাতিক মানের প্রোডাকশন।",
            iconicDialogue = "যে আমার সামনে আসবে, সে বরবাদ হবে!",
            trailerUrl = "https://www.youtube.com/results?search_query=Borbaad+Shakib+Khan+Teaser",
            posterDrawable = R.drawable.sk_toofan_poster,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_leader",
            titleBangla = "লিডার আমিই বাংলাদেশ",
            titleEnglish = "Leader Amie Bangladesh",
            year = "২০২৩",
            director = "তপু খান",
            coStar = "শব্নম বুবলী, মিশা সওদাগর",
            genre = "সোশ্যাল পলিটিক্যাল থ্রিলার",
            boxOffice = "সুপারহিট",
            rating = "৮.৫/১০",
            synopsis = "দেশের অন্যায়ের বিরুদ্ধে এক যুবকের প্রতিবাদ এবং নেতা হয়ে ওঠার বিপ্লবের গল্প।",
            iconicDialogue = "কথা নয়, কাজে বিশ্বাসী আমরা। আমিই বাংলাদেশ!",
            trailerUrl = "https://www.youtube.com/results?search_query=Leader+Amie+Bangladesh+Trailer",
            posterDrawable = R.drawable.sk_hero_banner,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_shikari",
            titleBangla = "শিকারী (Shikari)",
            titleEnglish = "Shikari",
            year = "২০১৬",
            director = "জয়দীপ মুখার্জী",
            coStar = "শ্রাবন্তী চ্যাটার্জী, সব্যসাচী চক্রবর্তী",
            genre = "ইন্ডো-বাংলা অ্যাকশন থ্রিলার",
            boxOffice = "অল-টাইম রেকর্ড ব্রেকিং হিট",
            rating = "৯.০/১০",
            synopsis = "শাকিব খানের ক্যারিয়ারের যুগান্তকারী লুক পরিবর্তন ও আন্তর্জাতিক মানের অ্যাকশন সিনেমা।",
            iconicDialogue = "শিকারী শিকার করার আগে আওয়াজ দেয় না, নিঃশব্দে ধুলোয় মিশিয়ে দেয়!",
            trailerUrl = "https://www.youtube.com/results?search_query=Shikari+Shakib+Khan+Trailer",
            posterDrawable = R.drawable.sk_toofan_poster,
            isBlockbuster = true
        ),
        Movie(
            id = "movie_nabab",
            titleBangla = "নবাব (Nabab)",
            titleEnglish = "Nabab",
            year = "২০১৭",
            director = "জয়দীপ মুখার্জী",
            coStar = "শুভশ্রী গাঙ্গুলি, অমিত হাসান",
            genre = "অ্যাকশন ড্রামা",
            boxOffice = "মেগা হিট",
            rating = "৮.৯/১০",
            synopsis = "বুদ্ধিমান সিবিআই অফিসার নবাবের সন্ত্রাসবাদের বিরুদ্ধে এক রক্তক্ষয়ী লড়াই।",
            iconicDialogue = "আমি যখন ময়দানে নামি, তখন ইতিহাস নতুন করে লেখা হয়!",
            trailerUrl = "https://www.youtube.com/results?search_query=Nabab+Shakib+Khan+Trailer",
            posterDrawable = R.drawable.sk_toofan_poster,
            isBlockbuster = true
        )
    )

    // Hit Songs
    fun getHitSongs(): List<Song> = listOf(
        Song(
            id = "song_uradhura",
            title = "লাগে উরাধুড়া (Laage Uradhura)",
            movie = "তুফান (Toofan)",
            singers = "প্রীতম হাসান ও দেবশ্রী অন্তরা",
            lyricist = "রাসেল মাহমুদ ও শরীফ উদ্দিন",
            duration = "৩:৪৫ মিনিট",
            lyricsSnippet = "চোখে চশমা মুখে হাসি, শাকিব ভাইরে বড্ড ভালোবাসি! লাগে উরাধুড়া ধামাকা...",
            youtubeUrl = "https://www.youtube.com/results?search_query=Laage+Uradhura+Toofan+Shakib+Khan",
            viewsCount = "১২০ মিলিয়ন+ ভিউজ"
        ),
        Song(
            id = "song_ishwar",
            title = "ঈশ্বর (Ishwar)",
            movie = "প্রিয়তমা (Priyotoma)",
            singers = "রিয়াদ",
            lyricist = "সোমেশ্বর অলি",
            duration = "৪:২০ মিনিট",
            lyricsSnippet = "ঈশ্বর কি তোমার আমার মিলন লিখে নাই? কেন এমন নিঠুর খেলায় কষ্ট পেয়ে যাই...",
            youtubeUrl = "https://www.youtube.com/results?search_query=Ishwar+Priyotoma+Shakib+Khan",
            viewsCount = "৯৫ মিলিয়ন+ ভিউজ"
        ),
        Song(
            id = "song_priyotoma",
            title = "প্রিয়তমা টাইটেল ট্র্যাক",
            movie = "প্রিয়তমা (Priyotoma)",
            singers = "বালাম ও কোনাল",
            lyricist = "আসিফ ইকবাল",
            duration = "৪:৫৫ মিনিট",
            lyricsSnippet = "কথা একটাই তুমি আমার প্রিয়তমা, তোমার জন্য জীবনটা করলাম জমা...",
            youtubeUrl = "https://www.youtube.com/results?search_query=Priyotoma+Title+Track+Balam+Konal",
            viewsCount = "১৪০ মিলিয়ন+ ভিউজ"
        ),
        Song(
            id = "song_dushtu_kokil",
            title = "দুষ্টু কোকিল (Dushtu Kokil)",
            movie = "তুফান (Toofan)",
            singers = "দিলশাদ নাহার কনা ও আকাশ সেন",
            lyricist = "আকাশ সেন",
            duration = "৩:৩০ মিনিট",
            lyricsSnippet = "মনের খাঁচায় ডাকলো যে এক দুষ্টু কোকিল কুহু কুহু...",
            youtubeUrl = "https://www.youtube.com/results?search_query=Dushtu+Kokil+Toofan+Shakib+Khan",
            viewsCount = "১১০ মিলিয়ন+ ভিউজ"
        ),
        Song(
            id = "song_borbaad_theme",
            title = "বরবাদ থিম ট্রাক (Borbaad Theme)",
            movie = "বরবাদ (Borbaad)",
            singers = "রক মিউজিক কম্পোজিশন",
            lyricist = "মুভি সাউন্ডট্র্যাক",
            duration = "২:৫০ মিনিট",
            lyricsSnippet = "রক্তে নেশা, চোখে আগুন... ধ্বংসের নাম শাকিব খান!",
            youtubeUrl = "https://www.youtube.com/results?search_query=Borbaad+Theme+Music+Shakib+Khan",
            viewsCount = "৩০ মিলিয়ন+ ভিউজ"
        )
    )

    // Famous Dialogues
    fun getFamousDialogues(): List<Dialogue> = listOf(
        Dialogue(
            id = "dlg_1",
            quote = "আমি কোনো সাধারণ মানুষ না, আমি তুফান!",
            movie = "তুফান (Toofan)",
            character = "গালিব / তুফান",
            context = "ক্লাইম্যাক্সে শত্রুদের মুখোমুখি দাঁড়িয়ে তীব্র গর্জন"
        ),
        Dialogue(
            id = "dlg_2",
            quote = "ভালোবাসা যখন নিঃস্বার্থ হয়, তখন ঈশ্বরও মাথা নত করতে বাধ্য হয়!",
            movie = "প্রিয়তমা (Priyotoma)",
            character = "সুমন",
            context = "প্রিয়তমার জন্য আত্মত্যাগের চরম মুহূর্তে"
        ),
        Dialogue(
            id = "dlg_3",
            quote = "শিকারী শিকার করার আগে আওয়াজ দেয় না, নিঃশব্দে এসে ধুলোয় মিশিয়ে দেয়!",
            movie = "শিকারী (Shikari)",
            character = "সুলতান",
            context = "শত্রু দলের ঘাঁটিতে ঢুকে একাই চ্যালেঞ্জ ছোঁড়া"
        ),
        Dialogue(
            id = "dlg_4",
            quote = "আমি যখন ময়দানে নামি, তখন ইতিহাস নতুন করে লেখা হয়!",
            movie = "নবাব (Nabab)",
            character = "নবাব",
            context = "দুর্নীতিবাজ গ্যাংস্টারদের হুশিয়ারি প্রদান"
        ),
        Dialogue(
            id = "dlg_5",
            quote = "কথা নয়, কাজে বিশ্বাসী আমরা। আমিই বাংলাদেশ!",
            movie = "লিডার আমিই বাংলাদেশ",
            character = "নাফিস",
            context = "জনসভার সামনে দাঁড়িয়ে অন্যায়ের বিরুদ্ধে গর্জন"
        ),
        Dialogue(
            id = "dlg_6",
            quote = "যার কাছে নিজের স্বপ্ন আছে, সে দুনিয়ার যেকোনো প্রান্তে গিয়ে লড়াই জিততে পারে!",
            movie = "রাজকুমার (Rajkumar)",
            character = "শামসু",
            context = "নিউ ইয়র্কে স্বপ্নপূরণের লড়াইয়ে বন্ধুদের প্রেরণা"
        )
    )

    // Latest News and Updates
    fun getLatestNews(): List<NewsItem> = listOf(
        NewsItem(
            id = "news_1",
            title = "টিকটকে 'লাগে উরাধুড়া' ট্রেন্ডে শতকোটি ভিউজের নতুন মাইলফলক",
            summary = "শাকিব খানের 'তুফান' সিনেমার গান নিয়ে টিকটক ও ফেসবুকে বিশ্বজুড়ে তৈরি হয়েছে লাখো রিলস ও ভিডিও।",
            date = "আজকের তাজা খবর",
            category = "টিকটক ভাইরাল",
            source = "শাকিবিয়ান মিডিয়া",
            url = "https://www.tiktok.com/@shakibkhan_official"
        ),
        NewsItem(
            id = "news_2",
            title = "আসন্ন ধামাকা 'বরবাদ' সিনেমার শুটিংয়ে শাকিব খানের দুধর্ষ স্টান্ট",
            summary = "আন্তর্জাতিক অ্যাকশন ডিরেক্টরের তত্ত্বাবধানে কোনো ডামি ছাড়াই নিজের সব অ্যাকশন দৃশ্য সম্পন্ন করলেন কিং খান।",
            date = "গতকাল",
            category = "সিনেমা আপডেট",
            source = "বিনোদন বার্তা",
            url = "https://www.facebook.com/shakibkhanofficial"
        ),
        NewsItem(
            id = "news_3",
            title = "শাকিব খানের ভেরিফাইড ফেসবুক ও টেলিগ্রাম চ্যানেলে যুক্ত হলেন লাখো ফ্যান",
            summary = "বিশ্বের বিভিন্ন প্রান্তের বাংলা সিনেমাপ্রেমীরা শাকিব খানের অফিসিয়াল সোশ্যাল প্ল্যাটফর্মে যোগ দিয়ে শুভকামনা জানাচ্ছেন।",
            date = "সাম্প্রতিক",
            category = "সোশ্যাল মিডিয়া",
            source = "এসকে ফ্যান ক্লাব",
            url = "https://t.me/shakibkhanhub"
        ),
        NewsItem(
            id = "news_4",
            title = "বিশ্বজুড়ে ঢালিউডের নতুন পরাশক্তি: আন্তর্জাতিক ফেস্টিভ্যালে পুরস্কৃত শাকিব খান",
            summary = "আন্তর্জাতিক সম্মাননা মঞ্চে ঢালিউড ইন্ডাস্ট্রিকে অনন্য উচ্চতায় তুলে ধরার জন্য সেরা নায়কের সম্মাননা।",
            date = "বিশেষ প্রতিবেদন",
            category = "পুরস্কার",
            source = "সিনেমা এক্সপ্রেস",
            url = "https://www.youtube.com/@SKFilmsOfficial"
        )
    )

    // Gallery Wallpapers
    fun getGalleryItems(): List<GalleryItem> = listOf(
        GalleryItem(
            id = "gal_1",
            title = "তুফান রয়্যাল লুক (Toofan Retro Swag)",
            tag = "মুভি ওয়ালপেপার",
            drawableRes = R.drawable.sk_toofan_poster,
            description = "তুফান সিনেমায় শাকিব খানের রেট্রো মাফিয়া অবতার।"
        ),
        GalleryItem(
            id = "gal_2",
            title = "কিং খান হিরো অবতার (King Khan Megastar)",
            tag = "এইচডি পোর্ট্রেট",
            drawableRes = R.drawable.sk_hero_banner,
            description = "ব্লকবাস্টার সিনেমার অ্যাকশন পোজে শাকিব খানের আকর্ষণীয় লুক।"
        ),
        GalleryItem(
            id = "gal_3",
            title = "শাকিবিয়ান ফ্যান স্টেডিয়াম উন্মাদনা",
            tag = "ফ্যান মিটআপ",
            drawableRes = R.drawable.sk_fan_community,
            description = "লাখো ভক্তের সামনে শাকিব খানের আগমন ও কনসার্ট ভিআইপি স্টেজ।"
        ),
        GalleryItem(
            id = "gal_4",
            title = "গোল্ডেন ক্রাউন লোগো এমব্লেম",
            tag = "ব্র্যান্ড ব্যাজ",
            drawableRes = R.drawable.ic_sk_launcher,
            description = "কিং খান শাকিব খানের রয়েল গোল্ডেন রাজকীয় মুকুট ব্যাজ।"
        )
    )
}
