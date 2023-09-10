package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object EnableMasterFilter : YukiBaseHooker() {
    override fun onHook() {
        //Source FilterGroupManager
        findClass("com.oplus.camera.filter.FilterGroupManager").hook {
            injectMember {
                method { name = "initVideoFilterGroup" }
                afterHook {
                    val mbBasicAndStyleNotEmpty = field { name = "mbBasicAndStyleNotEmpty" }.get()
                        .cast<Boolean>() ?: return@afterHook
                    if (mbBasicAndStyleNotEmpty) return@afterHook
                    val sVideoFilterGroup = field { name = "sVideoFilterGroup" }.get().any()
                        ?: return@afterHook
                    method {
                        name = "initMasterFilterGroup";paramCount = 2
                    }.get().call("com.oplus.photo.master.filter.type.list", sVideoFilterGroup)
                }
            }
            injectMember {
                method { name = "initStickerFilterGroup" }
                afterHook {
                    val mbBasicAndStyleNotEmpty = field { name = "mbBasicAndStyleNotEmpty" }.get()
                        .cast<Boolean>() ?: return@afterHook
                    if (mbBasicAndStyleNotEmpty) return@afterHook
                    val sStickerFilterGroup = field { name = "sStickerFilterGroup" }.get().any()
                        ?: return@afterHook
                    method {
                        name = "initMasterFilterGroup";paramCount = 2
                    }.get().call("com.oplus.photo.master.filter.type.list", sStickerFilterGroup)
                }
            }
        }
    }
}