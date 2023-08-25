package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

@Suppress("UNUSED_VARIABLE")
object ForceAllAppsSupportSplitScreen : YukiBaseHooker() {
    override fun onHook() {
        var isEnable = prefs(ModulePrefs).getBoolean("force_all_apps_support_split_screen", false)
        dataChannel.wait<Boolean>("force_all_apps_support_split_screen") { isEnable = it }
        if (SDK < A13) return
        //Source OplusSplitScreenManagerService
        findClass("com.android.server.wm.OplusSplitScreenManagerService").hook {
            injectMember {
                method { name = "isInBlackList" }
                if (isEnable) replaceToFalse()
            }
            injectMember {
                method { name = "supportsSplitScreenByVendorPolicy";paramCount = 3 }
                beforeHook {
                    if (!isEnable) return@beforeHook
                    val packageName = args().first().string()
                    val activityName = args(1).string()
                    val candidate = args().last().boolean()
                    val isSafeSenterUI = method {
                        name = "isSafeSenterUI";paramCount = 1
                    }.get(instance).invoke<Boolean>(activityName)
                    val isInForbidActivityList =
                        "com.android.server.wm.OplusSplitScreenAppReader".toClass()
                            .method { name = "getInstance" }.get().call()?.current()
                            ?.method { name = "isInForbidActivityList";paramCount = 1 }
                            ?.invoke<Boolean>(activityName)
                    if (isSafeSenterUI == true) resultFalse()
                    else if (isInForbidActivityList == true) resultFalse()
                    else resultTrue()
                }
            }
        }
    }
}