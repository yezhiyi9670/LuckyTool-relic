package com.luckyzyx.luckytool.hook.scope.externalstorage

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStorageLimit : YukiBaseHooker(){
    override fun onHook() {
        //Source ExternalStorageProvider
        findClass("com.android.externalstorage.ExternalStorageProvider").hook {
            injectMember {
                method {
                    name = "shouldBlockFromTree"
                }
                replaceToFalse()
            }
        }
    }
}