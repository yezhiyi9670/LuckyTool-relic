package com.luckyzyx.luckytool.utils

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppInfo(
    var appIcon: Drawable,
    var appName: CharSequence,
    var packName: String,
) : Serializable

@Suppress("ArrayInDataClass")
data class DInfo(
    val name: String,
    val details: Array<DCInfo>
) : Serializable

data class DCInfo(
    val time: String,
    val channel: String,
    val money: Double,
    val order: String,
    val unit: String = "RMB"
) : Serializable