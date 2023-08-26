package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.replaceSpace

object RemoveSmallWindowReplyWhitelist : YukiBaseHooker() {
    override fun onHook() {
        var list = prefs(ModulePrefs).getString("set_small_window_reply_blacklist", "None")
        dataChannel.wait<String>("set_small_window_reply_blacklist") { list = it }
        //Source BaseNotificationContentInflater
        VariousClass(
            "com.oplusos.systemui.notification.base.BaseNotificationContentInflater", //C13
            "com.oplus.systemui.statusbar.NotificationListenerExtImpl" //C14
        ).hook {
            injectMember {
                method { name = "showSmallWindowReply" }
                afterHook {
                    val packName = args().first().string()
                    if (list.isBlank() || list == "None") resultTrue()
                    else {
                        val listString = list.replaceSpace
                        val blacklist = if (list.contains("\n")) {
                            listString.split("\n").toMutableList().apply {
                                removeIf { it.isBlank() }
                            }
                        } else arrayListOf(listString)
                        if (blacklist.contains(packName)) resultFalse()
                    }
                }
            }
        }
    }
}