package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.allViews
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.TextViewClass
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode

object LockScreenChargingComponent : YukiBaseHooker() {
    override fun onHook() {
        when (getOSVersionCode) {
            30 -> loadHooker(ChargingComponentC14)
            in 26..29 -> loadHooker(ChargingComponentC13)
            else -> loadHooker(ChargingComponentC12)
        }
    }

    private object ChargingComponentC14 : YukiBaseHooker() {
        override fun onHook() {
            var userTypeface =
                prefs(ModulePrefs).getBoolean("lock_screen_charging_use_user_typeface", false)
            dataChannel.wait<Boolean>("lock_screen_charging_use_user_typeface") {
                userTypeface = it
            }
//            var warpCharge =
//                prefs(ModulePrefs).getString("set_lock_screen_warp_charging_style", "0")
//            dataChannel.wait<String>("set_lock_screen_warp_charging_style") { warpCharge = it }
            var textLogo =
                prefs(ModulePrefs).getString("set_lock_screen_charging_text_logo_style", "0")
            dataChannel.wait<String>("set_lock_screen_charging_text_logo_style") { textLogo = it }
//            var showWattage =
//                prefs(ModulePrefs).getBoolean("force_lock_screen_charging_show_wattage", false)
//            dataChannel.wait<Boolean>("force_lock_screen_charging_show_wattage") {
//                showWattage = it
//            }

            //Source ChargingLevelAndLogoView
            findClass("com.oplus.charge.view.ChargeLevelAndLogoView").hook {
                injectMember {
                    method { param(TypefaceClass) }
                    afterHook {
                        if (!userTypeface) return@afterHook
                        instance<LinearLayout>().allViews.forEach {
                            if (it.javaClass == TextViewClass) {
                                (it as TextView).typeface = Typeface.DEFAULT_BOLD
                            }
                        }
                    }
                }
                injectMember {
                    method { name = "showTextLogo" }
                    beforeHook {
                        when (textLogo) {
                            "1" -> resultTrue()
                            "2" -> resultFalse()
                            else -> return@beforeHook
                        }
                    }
                }
            }
        }
    }

    private object ChargingComponentC13 : YukiBaseHooker() {
        override fun onHook() {
            var userTypeface =
                prefs(ModulePrefs).getBoolean("lock_screen_charging_use_user_typeface", false)
            dataChannel.wait<Boolean>("lock_screen_charging_use_user_typeface") {
                userTypeface = it
            }
            var warpCharge =
                prefs(ModulePrefs).getString("set_lock_screen_warp_charging_style", "0")
            dataChannel.wait<String>("set_lock_screen_warp_charging_style") { warpCharge = it }
            var textLogo =
                prefs(ModulePrefs).getString("set_lock_screen_charging_text_logo_style", "0")
            dataChannel.wait<String>("set_lock_screen_charging_text_logo_style") { textLogo = it }
            var showWattage =
                prefs(ModulePrefs).getBoolean("force_lock_screen_charging_show_wattage", false)
            dataChannel.wait<Boolean>("force_lock_screen_charging_show_wattage") {
                showWattage = it
            }

            //Source ChargingLevelAndLogoView
            findClass("com.oplusos.systemui.keyguard.charginganim.siphonanim.ChargingLevelAndLogoView").hook {
                injectMember {
                    method { name = "updatePowerFormat" }
                    afterHook {
                        if (!userTypeface) return@afterHook
                        instance<LinearLayout>().allViews.forEach {
                            if (it.javaClass == TextViewClass) {
                                (it as TextView).typeface = Typeface.DEFAULT_BOLD
                            }
                        }
                    }
                }
                injectMember {
                    method { name = "showTextLogo" }
                    beforeHook {
                        if (warpCharge != "2") return@beforeHook
                        when (textLogo) {
                            "1" -> resultTrue()
                            "2" -> resultFalse()
                            else -> return@beforeHook
                        }
                    }
                }
            }

            //Source ChargingAnimationImpl
            findClass("com.oplusos.systemui.keyguard.charginganim.ChargingAnimationImpl").hook {
                injectMember {
                    method { name = "isMaxWattageMatchs" }
                    beforeHook {
                        if (warpCharge != "2") return@beforeHook
                        val mChargerWattage = field { name = "mChargerWattage" }.get(instance).int()
                        if (showWattage && (mChargerWattage != 0)) resultTrue()
                    }
                }
                injectMember {
                    method { name = "isUFCSCharging" }
                    afterHook {
                        loggerD(msg = "isUFCSCharging -> $result")

                        val mIsPublicUFCSCharging =
                            field { name = "mIsPublicUFCSCharging" }.get(instance).cast<Boolean>()
                        val mIsPrivateUFCSCharging =
                            field { name = "mIsPrivateUFCSCharging" }.get(instance).cast<Boolean>()
                        loggerD(msg = "IsPublicUFCSCharging -> $mIsPublicUFCSCharging")
                        loggerD(msg = "IsPrivateUFCSCharging -> $mIsPrivateUFCSCharging")
                    }
                }
//                injectMember {
//                    method { name = "isPublicUFCSCharging" }
//                    afterHook {
//                        loggerD(msg = "isPublicUFCSCharging -> $result")
//                    }
//                }
//                injectMember {
//                    method { name = "isPrivateUFCSCharging" }
//                    afterHook {
//                        loggerD(msg = "isPrivateUFCSCharging -> $result")
//                    }
//                }
            }

            val clazz =
                "com.oplusos.systemui.keyguard.charginganim.siphonanim.flavorone.ChargingLevelAndLogoViewForFlavorOneVfx"
            if (clazz.toClassOrNull() == null) return
            //Source ChargingLevelAndLogoViewForFlavorOneVfx
            findClass(clazz).hook {
                injectMember {
                    method { name = "setTypeface" }
                    afterHook {
                        if (!userTypeface) return@afterHook
                        instance<LinearLayout>().allViews.forEach {
                            if (it.javaClass == TextViewClass) {
                                (it as TextView).typeface = Typeface.DEFAULT_BOLD
                            }
                        }
                    }
                }
                injectMember {
                    method { name = "showTextLogo" }
                    beforeHook {
                        if (warpCharge != "2") return@beforeHook
                        when (textLogo) {
                            "1" -> resultTrue()
                            "2" -> resultFalse()
                            else -> return@beforeHook
                        }
                    }
                }
            }
        }
    }

    private object ChargingComponentC12 : YukiBaseHooker() {
        override fun onHook() {
            var userTypeface =
                prefs(ModulePrefs).getBoolean("lock_screen_charging_use_user_typeface", false)
            dataChannel.wait<Boolean>("lock_screen_charging_use_user_typeface") {
                userTypeface = it
            }
            var textLogo =
                prefs(ModulePrefs).getString("set_lock_screen_charging_text_logo_style", "0")
            dataChannel.wait<String>("set_lock_screen_charging_text_logo_style") { textLogo = it }
            var showWattage =
                prefs(ModulePrefs).getBoolean("force_lock_screen_charging_show_wattage", false)
            dataChannel.wait<Boolean>("force_lock_screen_charging_show_wattage") {
                showWattage = it
            }

            //Source ChargingLevelAndLogoView
            findClass("com.oplusos.systemui.keyguard.charginganim.siphonanim.ChargingLevelAndLogoView").hook {
                injectMember {
                    method { name = "updatePowerFormat" }
                    afterHook {
                        if (!userTypeface) return@afterHook
                        instance<LinearLayout>().allViews.forEach {
                            if (it.javaClass == TextViewClass) {
                                (it as TextView).typeface = Typeface.DEFAULT_BOLD
                            }
                        }
                    }
                }
                injectMember {
                    method { name = "isLocaleZhCN" }
                    beforeHook {
                        when (textLogo) {
                            "1" -> resultTrue()
                            "2" -> resultFalse()
                            else -> return@beforeHook
                        }
                    }
                }
            }

            //Source ChargingAnimationImpl
            findClass("com.oplusos.systemui.keyguard.charginganim.ChargingAnimationImpl").hook {
                injectMember {
                    method { name = "isSupportShowWattage" }
                    if (showWattage) replaceToTrue()
                }
            }
        }
    }
}