import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.inMemoryDatabaseDriver
import com.github.tokou.common.database.persistentDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    Window("HN") {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppTheme {
                val rootComponent = rememberRootComponent {
                    NewsRootComponent(
                        componentContext = it,
                        coroutineScope = this,
                        storeFactory = LoggingStoreFactory(DefaultStoreFactory),
                        database = createDatabase(inMemoryDatabaseDriver())
                    )
                }
                NewsRoot(component = rootComponent)
            }
        }
    }
}
