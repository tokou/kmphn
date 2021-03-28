package com.github.tokou.common.main

import app.cash.turbine.test
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.main.NewsMainStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class NewsMainStoreTest {

    class TestRepository : NewsMainStoreProvider.Repository {

        private val _updates = MutableSharedFlow<Result<List<News>>>(1)
        override val updates: Flow<Result<List<News>>> = _updates

        var items = emptyList<News>()

        override suspend fun load(loaded: Set<ItemId>, loadCount: Int) {
            items.forEach {
                _updates.tryEmit(Result.success(listOf(it)))
                delay(1)
            }
        }
    }

    lateinit var repository: TestRepository
    lateinit var provider: NewsMainStoreProvider

    @BeforeTest
    fun before() {
        repository = TestRepository()
        provider = NewsMainStoreProvider(DefaultStoreFactory, repository)
    }

    private fun news(id: ItemId, title: String? = null) =
        News(id, title, null, null, Instant.fromEpochSeconds(0), 0, 0)

    @Test
    fun testInitialAction() = runBlocking {
        repository.items = listOf(news(1, "DB"), news(1, "API"))
        val store = provider.provide()
        store.states.test {
            val expected = listOf(
                State.Loading,
                State.Content(listOf(news(1, "DB"))),
                State.Content(listOf(news(1, "API"))),
            )
            for (it in expected) assertEquals(it, expectItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testRefreshIntent() = runBlocking {
        repository.items = listOf(news(1, "DB"), news(1, "API"))
        val store = provider.provide()
        store.states.test {
            val expected = listOf(
                State.Loading,
                State.Content(listOf(news(1, "DB"))),
                State.Content(listOf(news(1, "API"))),
            )
            for (it in expected) assertEquals(it, expectItem())
            repository.items = listOf(news(2, "DB"), news(2, "API"))
            store.accept(Intent.Refresh)
            val expected2 = listOf(
                State.Loading,
                State.Content(listOf(news(2, "DB"))),
                State.Content(listOf(news(2, "API"))),
            )
            for (it in expected2) assertEquals(it, expectItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
