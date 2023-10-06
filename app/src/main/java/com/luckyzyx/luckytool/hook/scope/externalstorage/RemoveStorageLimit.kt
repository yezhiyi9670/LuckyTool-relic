package com.luckyzyx.luckytool.hook.scope.externalstorage

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveStorageLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source ExternalStorageProvider
        "com.android.externalstorage.ExternalStorageProvider".toClass().apply {
            method { name = "shouldBlockFromTree" }.hook {
                replaceToFalse()
            }
        }
    }
}