package com.luckyzyx.luckytool.utils

import android.graphics.drawable.Drawable
import java.io.Serializable

val qbss
    get() = ArrayList<String>().apply {
        add("MTE1MDMyNTYxOQ==")
        add("OTA3OTg5MDU0")
        add("MzEwODQ0MDE4Mg==")
        add("MzQzMTI5OTA1OQ==")
    }
val cbss
    get() = ArrayList<String>().apply {
        add("MTMwNDQ4MA==")
        add("MTYxNDk5MDg=")
    }

data class AppInfo(
    var appIcon: Drawable,
    var appName: CharSequence,
    var packName: String,
) : Serializable