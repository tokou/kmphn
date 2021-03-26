package com.github.tokou.android

import android.content.ComponentName
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.browser.customtabs.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class CustomTabsHelper(private val context: Context) {

    private lateinit var client: CustomTabsClient
    private var session: CustomTabsSession? = null

    private val connection = object : CustomTabsServiceConnection() {
        override fun onServiceDisconnected(name: ComponentName?) {}

        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            this@CustomTabsHelper.client = client
            client.warmup(0)
            this@CustomTabsHelper.session = client.newSession(null)
        }
    }

    private val packageName = "com.android.chrome"

    fun bind() = CustomTabsClient.bindCustomTabsService(context, packageName, connection)

    fun createUriHandler(color: Color): (String) -> Unit = { uri: String ->
        val arrowIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_arrow_back)
        val colorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(color.toArgb())
            .build()
        CustomTabsIntent.Builder(session)
            .setDefaultColorSchemeParams(colorParams)
            .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
            .setCloseButtonIcon(arrowIcon)
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .setInstantAppsEnabled(true)
            .build()
            .launchUrl(context, Uri.parse(uri))
    }
}
