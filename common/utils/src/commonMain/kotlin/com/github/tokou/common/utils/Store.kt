package com.github.tokou.common.utils

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.ValueObserver
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> Store<*, T, *>.asValue(coroutineScope: CoroutineScope): Value<T> =
    object : Value<T>() {
        override val value: T get() = state
        private var jobs = emptyMap<ValueObserver<T>, Job>()

        override fun subscribe(observer: ValueObserver<T>) {
            val job = states
                .onEach { observer(it) }
                .launchIn(coroutineScope)
            this.jobs += observer to job
        }

        override fun unsubscribe(observer: ValueObserver<T>) {
            val job = jobs[observer] ?: return
            this.jobs -= observer
            job.cancel()
        }
    }
