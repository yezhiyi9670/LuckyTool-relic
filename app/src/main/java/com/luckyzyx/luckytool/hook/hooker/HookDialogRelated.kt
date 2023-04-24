package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.hook.scope.exsystemservice.RemoveWarningDialogThatAppRunsOnDesktop
import com.luckyzyx.luckytool.hook.scope.securepay.RemoveSecurePayFoundVirusDialog
import com.luckyzyx.luckytool.hook.scope.systemui.DisableDuplicateFloatingWindow
import com.luckyzyx.luckytool.hook.scope.systemui.DisableHeadphoneHighVolumeWarning
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveLowBatteryDialogWarning
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveUSBConnectDialog
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookDialogRelated : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.android.systemui") {
            //禁用复制悬浮窗
            if (prefs(ModulePrefs).getBoolean("disable_duplicate_floating_window", false)) {
                if (SDK >= A13) loadHooker(DisableDuplicateFloatingWindow)
            }
            //禁用耳机高音量警告
            if (prefs(ModulePrefs).getBoolean("disable_headphone_high_volume_warning", false)) {
                loadHooker(DisableHeadphoneHighVolumeWarning)
            }
            //移除低电量对话框警告
            if (prefs(ModulePrefs).getBoolean("remove_low_battery_dialog_warning", false)) {
                loadHooker(RemoveLowBatteryDialogWarning)
            }
            //移除USB连接对话框
            if (prefs(ModulePrefs).getBoolean("remove_usb_connect_dialog", false)) {
                loadHooker(RemoveUSBConnectDialog)
            }
        }

        if (packageName == "com.oplus.exsystemservice") {
            //移除应用运行在桌面上警告对话框
            if (prefs(ModulePrefs).getBoolean(
                    "remove_warning_dialog_that_app_runs_on_desktop", false
                )
            ) {
                loadHooker(RemoveWarningDialogThatAppRunsOnDesktop)
            }
        }

        if (packageName == "com.coloros.securepay") {
            //移除支付保护发现病毒对话框
            if (prefs(ModulePrefs).getBoolean("remove_secure_pay_found_virus_dialog", false)) {
                loadHooker(RemoveSecurePayFoundVirusDialog)
            }
        }

        @Suppress("UNREACHABLE_CODE")
        if (packageName == "com.coloros.phonemanager") {
            return
            //扫描结果发送广播
            //t(context, "com.oppo.safe.action.VIRUS_FOUND", i10, arrayList, arrayList2);
            //t(context, "oplus.intent.action.VIRUS_FOUND", i10, arrayList, arrayList2);
            //Source VirusCommonUtils
            findClass("com.coloros.phonemanager.virusdetect.util.g").hook {
                injectMember {
                    method {
                        name = "s"
                        paramCount = 4
                    }
                    beforeHook {
                        val list = args(2).cast<java.util.ArrayList<Any>>()
                        val list2 = args(3).cast<java.util.ArrayList<Any>>()
                        if (list != null) {
                            for (i in list) {
                                val names = i.current().field {
                                    name = "name"
                                }.string()
                                val pkgName = i.current().field {
                                    name = "pkgName"
                                }.string()
                                loggerD(msg = "list $names $pkgName")
                            }
                        }
                        if (list2 != null) {
                            for (i in list2) {
                                val names = i.current().field {
                                    name = "name"
                                }.string()
                                val pkgName = i.current().field {
                                    name = "pkgName"
                                }.string()
                                loggerD(msg = "list2 $names $pkgName")
                            }
                        }
                    }
                }
            }
        }

        //分享
        //com.android.internal.app.ChooserActivity

    }
}