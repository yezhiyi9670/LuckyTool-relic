package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ArrayMapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomAonGestureScrollPageWhitelist : YukiBaseHooker() {
    override fun onHook() {

        val scrollList =
            prefs(ModulePrefs).getString("custom_aon_gesture_scroll_page_whitelist", "None")
//        val videoList = prefs(ModulePrefs).getString("custom_aon_gesture_video_whitelist", "None")

        if (scrollList.isBlank() || scrollList == "None") return

        //Search com.ss.android.ugc.aweme / com.smile.gifmaker
        searchClass {
            from("p6", "r6", "r5", "z5").absolute()
            field { type = ContextClass }.count(1)
            field { type = ArrayListClass }.count(1)
            field { type = ArrayMapClass }.count(1)
            field { type = IntType }.count(2)
            field { type = FloatType }.count(3)
            field { type = ListClass }.count(3..4)
            method { param(StringClass);returnType = IntType }.count(6)
            method { param(ListClass);returnType = UnitType }.count(2)
        }.get()?.hook {
            injectMember {
                method { emptyParam();returnType = ListClass }.all()
                afterHook {
                    val field = result<List<String>>() ?: return@afterHook
                    if (field.isEmpty()) return@afterHook
                    result = field.toMutableList().apply {
                        if (contains("com.ss.android.ugc.aweme") || contains("com.smile.gifmaker")) {
                            if (scrollList.contains("\n")) {
                                scrollList.replace(" ", "").split("\n")
                                    .forEach { if (it.isNotBlank()) add(it) }
                            } else add(scrollList)
                        }
                    }
                }
            }
        }

        //Source GestureUtil
        findClass("com.oplus.gesture.util.GestureUtil").hook {
            injectMember {
                method { name = "getLocalAonAppListTurnPage" }
                afterHook {
                    val list = result<List<String>>() ?: return@afterHook
                    result = list.toMutableList().apply {
                        if (scrollList.contains("\n")) {
                            scrollList.replace(" ", "").split("\n")
                                .forEach { if (it.isNotBlank()) add(it) }
                        } else add(scrollList)
                    }
                }
            }
//            injectMember {
//                method { name = "getLocalAonAppListPauseOrPlay" }
//                afterHook {
//                    if (videoList.isBlank() || videoList == "None") return@afterHook
//                    val list = result<List<String>>() ?: return@afterHook
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