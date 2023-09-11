@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.os.Build
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.hook.utils.OplusBuildUtlils

/**SDK_INT版本*/
val SDK get() = Build.VERSION.SDK_INT

/**Android11 30 R*/
val A11 get() = Build.VERSION_CODES.R

/**Android12 31 S*/
val A12 get() = Build.VERSION_CODES.S

/**Android12.1 32 S_V2*/
val A121 get() = Build.VERSION_CODES.S_V2

/**Android13 33 TIRAMISU*/
val A13 get() = Build.VERSION_CODES.TIRAMISU

/**Android14 34 XX*/
val A14 get() = Build.VERSION_CODES.UPSIDE_DOWN_CAKE

/**
 * 获取构建版本名/版本号
 * @return [String]
 */
val getVersionName get() = BuildConfig.VERSION_NAME
val getVersionCode get() = BuildConfig.VERSION_CODE

/**
 * 获取OS版本名
 * V12
 * V12.1
 * V12.2
 * V13
 * V13.1
 * V13.1.1
 * V13.2
 * V14
 */
val getOSVersionName get() = safeOf("null") { OplusBuildUtlils().getOSVersionName ?: "null" }

/**
 * 获取OS版本号
 * 23 -> (c12)
 * 24 -> (c12.1)
 * 25 -> (c12.2)
 * 26 -> (c13)
 * 27 -> (c13.1)
 * 28 -> (c13.1.1)
 * 29 -> (c13.2)
 * 30 -> (c14)
 */
val getOSVersionCode get() = safeOf(0) { OplusBuildUtlils().getOSVersionCode ?: 0 }
