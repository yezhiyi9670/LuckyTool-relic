package com.luckyzyx.luckytool.utils.data

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppInfo(
    var appIcon: Drawable,
    var appName: CharSequence,
    var packName: String,
) : Serializable