import com.github.tokou.common.utils.logger
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

val appIcon: BufferedImage = ImageIO.read(object {}::class.java.getResource("/icon.png"))

fun openLink(uri: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()!!
        desktop.browse(URI(uri))
    }
}

fun isMacInDarkMode(): Boolean = try {
    val command = arrayOf("defaults", "read", "-g", "AppleInterfaceStyle")
    val proc = Runtime.getRuntime().exec(command)
    proc.waitFor(10, TimeUnit.MILLISECONDS)
    proc.exitValue() == 0
} catch (e: Exception) {
    logger.e("MAIN") {
        "Could not determine, whether 'dark mode' is being used. " +
            "Falling back to default (light) mode."
    }
    false
}
