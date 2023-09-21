package com.luckyzyx.luckytool.utils

import org.luckypray.dexkit.DexKitBridge

object DexkitUtils {
    /**
     * 创建Dexkit实例
     * @param appPath String
     * @return DexKitBridge?
     */
    fun create(appPath: String): DexKitBridge? {
        System.loadLibrary("dexkit")
        return DexKitBridge.create(appPath)
    }
}