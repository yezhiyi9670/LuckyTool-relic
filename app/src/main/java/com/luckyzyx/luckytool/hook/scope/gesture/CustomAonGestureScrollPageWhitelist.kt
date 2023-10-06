package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ArrayMapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.replaceSpace

object CustomAonGestureScrollPageWhitelist : YukiBaseHooker() {
    override fun onHook() {
        val scrollList =
            prefs(ModulePrefs).getString("custom_aon_gesture_scroll_page_whitelist", "None")
//        val videoList = prefs(ModulePrefs).getString("custom_aon_gesture_video_whitelist", "None")
        if (scrollList.isBlank() || scrollList == "None") return

        //Search com.ss.android.ugc.aweme / com.smile.gifmaker
        DexkitUtils.searchDexClass(
            "CustomAonGestureScrollPageWhitelist", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(ArrayListClass.name)
                        addForType(ArrayMapClass.name)
                        addForType(IntType.name)
                        addForType(FloatType.name)
                        addForType(ListClass.name)
                    }
                    methods {
                        add { paramTypes(StringClass.name);returnType(IntType.name) }
                        add { paramTypes(ListClass.name);returnType(UnitType.name) }
                    }
                    usingStrings("com.ss.android.ugc.aweme", "com.smile.gifmaker")
                }
            }
        }?.firstOrNull()?.className?.toClass()?.apply {
            method { emptyParam();returnType = ListClass }.giveAll().forEach {
                it.hook {
                    after {
                        val field = result<List<String>>() ?: return@after
                        if (field.isEmpty()) return@after
                        result = field.toMutableList().apply {
                            if (contains("com.ss.android.ugc.aweme") || contains("com.smile.gifmaker")) {
                                val listString = scrollList.replaceSpace
                                if (listString.contains("\n")) {
                                    listString.split("\n").forEach { s ->
                                        if (s.isNotBlank()) add(s)
                                    }
                                } else add(scrollList)
                            }
                        }
                    }
                }
            }
        }

        //Source GestureUtil
        "com.oplus.gesture.util.GestureUtil".toClass().apply {
            method { name = "getLocalAonAppListTurnPage" }.hook {
                after {
                    val list = result<List<String>>() ?: return@after
                    result = list.toMutableList().apply {
                        val listString = scrollList.replaceSpace
                        if (listString.contains("\n")) {
                            listString.split("\n").forEach { if (it.isNotBlank()) add(it) }
                        } else add(scrollList)
                    }
                }
            }
//            injectMember {
//                method { name = "getLocalAonAppListPauseOrPlay" }
//                after {
//                    if (videoList.isBlank() || videoList == "None") return@after
//                    val list = result<List<String>>() ?: return@after
//                    result = list.toMutableList().apply {
//                        if (videoList.contains("\n")) {
//                            videoList.replace(" ", "").split("\n")
//                                .forEach { if (it.isNotBlank()) add(it) }
//                        } else add(videoList)
//                    }
//                }
//            }
        }
    }
}