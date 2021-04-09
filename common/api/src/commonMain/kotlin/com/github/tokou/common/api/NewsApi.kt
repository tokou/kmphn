package com.github.tokou.common.api

import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.Timestamp
import com.github.tokou.common.utils.UserId
import com.github.tokou.common.utils.logNetwork
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import com.github.tokou.common.utils.logger as Logger


private val json = Json {
    ignoreUnknownKeys = true
}

fun createApi(): NewsApi {
    val baseUrl = "https://hacker-news.firebaseio.com/v0"
    val client = createHttpClient(json)
    return NewsApi(baseUrl, client)
}

fun createHttpClient(json: Json) = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(json)
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Logger.d("KTOR") { message }
            }
        }
        level = if (logNetwork) LogLevel.ALL else LogLevel.NONE
    }
    defaultRequest {
        header("Connection", "close")
    }
}

// https://hackernews.api-docs.io/v0/overview/introduction
class NewsApi(private val baseUrl: String, private val client: HttpClient) {

    @Serializable
    data class Updates(val items: List<Long>, val profiles: List<String>)

    @Serializable
    data class User(val id: String, val created: Long, val karma: Long, val about: String? = null, val submitted: List<Long> = emptyList())

    @Serializable
    sealed class Item {
        abstract val id: ItemId
        abstract val deleted: Boolean
        abstract val by: UserId?
        abstract val time: Timestamp
        abstract val dead: Boolean
        abstract val kids: List<ItemId>

        @Serializable
        @SerialName("job")
        data class Job(
            override val id: ItemId,
            override val deleted: Boolean = false,
            override val by: UserId? = null,
            override val time: Timestamp = 0,
            override val dead: Boolean = false,
            override val kids: List<ItemId> = emptyList(),
            val text: String? = null,
            val url: String? = null,
            val title: String? = null,
        ): Item()

        @Serializable
        @SerialName("story")
        data class Story(
            override val id: ItemId,
            override val deleted: Boolean = false,
            override val by: UserId? = null,
            override val time: Timestamp = 0,
            override val dead: Boolean = false,
            override val kids: List<ItemId> = emptyList(),
            val text: String? = null,
            val descendants: Long = 0,
            val score: Long = 0,
            val title: String? = null,
            val url: String? = null,
        ): Item()

        @Serializable
        @SerialName("comment")
        data class Comment(
            override val id: ItemId,
            override val deleted: Boolean = false,
            override val by: UserId? = null,
            override val time: Timestamp = 0,
            override val dead: Boolean = false,
            override val kids: List<ItemId> = emptyList(),
            val parent: ItemId? = null,
            val text: String? = null,
        ): Item()

        @Serializable
        @SerialName("poll")
        data class Poll(
            override val id: ItemId,
            override val deleted: Boolean = false,
            override val by: UserId? = null,
            override val time: Timestamp = 0,
            override val dead: Boolean = false,
            override val kids: List<ItemId> = emptyList(),
            val parts: List<ItemId> = emptyList(),
            val descendants: Long = 0,
            val score: Long = 0,
            val title: String? = null,
            val text: String? = null,
        ): Item()

        @Serializable
        @SerialName("pollopt")
        data class PollOption(
            override val id: ItemId,
            override val deleted: Boolean = false,
            override val by: UserId? = null,
            override val time: Timestamp = 0,
            override val dead: Boolean = false,
            override val kids: List<ItemId> = emptyList(),
            val parent: ItemId? = null,
            val score: Long = 0,
        ): Item()

    }

    suspend fun fetchMaxItemId() = client.get<Long>("$baseUrl/maxitem.json")
    suspend fun fetchTopStoriesIds() = client.get<List<Long>>("$baseUrl/topstories.json")
    suspend fun fetchJobStoriesIds() = client.get<List<Long>>("$baseUrl/jobstories.json")
    suspend fun fetchBestStoriesIds() = client.get<List<Long>>("$baseUrl/beststories.json")
    suspend fun fetchNewStoriesIds() = client.get<List<Long>>("$baseUrl/newstories.json")
    suspend fun fetchAskStoriesIds() = client.get<List<Long>>("$baseUrl/askstories.json")
    suspend fun fetchShowStoriesIds() = client.get<List<Long>>("$baseUrl/showstories.json")
    suspend fun fetchUpdates() = client.get<Updates>("$baseUrl/updates.json")
    suspend fun fetchUser(id: String) = client.get<User?>("$baseUrl/user/$id.json")
    // https://github.com/Kotlin/kotlinx.serialization/issues/1000#issuecomment-678983701
    suspend fun fetchItem(id: Long) = client.get<String?>("$baseUrl/item/$id.json")?.let { body ->
        if (body == "null") null
        else json.decodeFromString(Item.serializer(), body)
    }
}
