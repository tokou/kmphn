import androidx.compose.ui.platform.UriHandler
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.github.tokou.common.utils.logStore
import com.github.tokou.common.utils.logger

fun StoreFactory.logging(): LoggingStoreFactory = LoggingStoreFactory(this, storeLogger)

private val storeLogger = object : Logger {
    override fun log(text: String) {
        if (logStore) logger.d("STORE") { text }
    }
}

val uriHandler = object : UriHandler {
    override fun openUri(uri: String) = openLink(uri)
}
