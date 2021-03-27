import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.inMemoryDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.root.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme
import com.github.tokou.common.utils.logStore
import com.github.tokou.common.utils.logger
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.net.URI
import javax.imageio.ImageIO

val icon: BufferedImage = ImageIO.read(object {}::class.java.getResource("/icon.png"))

fun main() = Window(title = "HN", icon = icon) { App() }

fun openLink(uri: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()!!
        desktop.browse(URI(uri))
    }
}

val uriHandler = object : UriHandler {
    override fun openUri(uri: String) = openLink(uri)
}

private val storeLogger = object : Logger {
    override fun log(text: String) {
        if (logStore) logger.d("STORE") { text }
    }
}

private fun StoreFactory.logging(): LoggingStoreFactory = LoggingStoreFactory(this, storeLogger)

@Composable
fun App() {
    Surface(modifier = Modifier.fillMaxSize()) {
        AppTheme {
            val rootComponent = rememberRootComponent {
                NewsRootComponent(
                    componentContext = it,
                    uriHandler = ::openLink,
                    storeFactory = DefaultStoreFactory.logging(),
                    api = createApi(),
                    database = createDatabase(inMemoryDatabaseDriver())
                )
            }
            CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                NewsRoot(component = rootComponent)
            }
        }
    }
}
