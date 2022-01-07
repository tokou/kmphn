import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.persistentDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.root.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme

fun main() = singleWindowApplication(title = "HN") { ThemedFrame { App() } }

@Composable
fun ThemedFrame(content: @Composable () -> Unit) {
    AppTheme(isMacInDarkMode()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun App() {
    val rootComponent = rememberRootComponent {
        NewsRootComponent(
            componentContext = it,
            uriHandler = ::openLink,
            storeFactory = DefaultStoreFactory.logging(),
            api = createApi(),
            database = createDatabase(persistentDatabaseDriver())
        )
    }
    CompositionLocalProvider(LocalUriHandler provides uriHandler) {
        NewsRoot(component = rootComponent)
    }
}
