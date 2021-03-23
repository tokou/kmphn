package com.github.tokou.common.utils

import com.arkivanov.decompose.Router
import com.arkivanov.decompose.RouterFactory
import kotlinx.coroutines.CoroutineScope
import com.arkivanov.decompose.ComponentContext as DecomposeComponentContext
import com.arkivanov.decompose.statekeeper.Parcelable
import kotlinx.coroutines.GlobalScope
import kotlin.reflect.KClass

interface ComponentContext : DecomposeComponentContext, CoroutineScope

class DefaultComponentContext(
    componentContext: DecomposeComponentContext,
    coroutineScope: CoroutineScope
) : ComponentContext, DecomposeComponentContext by componentContext, CoroutineScope by coroutineScope

fun <C : Parcelable, T : Any> ComponentContext.router(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    componentFactory: (configuration: C, ComponentContext) -> T
): Router<C, T> =
    (this as RouterFactory).router(
        initialConfiguration = initialConfiguration,
        initialBackStack = initialBackStack,
        configurationClass = configurationClass,
        key = key,
        handleBackButton = handleBackButton
    ) { configuration, componentContext ->
        componentFactory(
            configuration,
            DefaultComponentContext(
                componentContext = componentContext,
                coroutineScope = GlobalScope
            )
        )
    }
