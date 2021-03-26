package com.github.tokou.android

import android.content.Context
import android.util.TypedValue

object ThemeHelper {
    fun ensureRuntimeTheme(context: Context) {
        val tv = TypedValue()
        context.theme.resolveAttribute(R.attr.runtimeTheme, tv, true)
        if (tv.resourceId <= 0) error("runtimeTheme not defined in the splash theme")
        context.setTheme(tv.resourceId)
    }
}
