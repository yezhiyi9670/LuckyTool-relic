package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass

object HookSettingsFeature : YukiBaseHooker() {
    override fun onHook() {
        val memcVideo = false
            //prefs(ModulePrefs).getBoolean("force_display_video_memc_frame_insertion", false)
        //Source SysFeatureUtils
        searchClass {
            from("oi", "ki", "ji").absolute()
            field().count { it > 30 }
            method {
                emptyParam()
                returnType = BooleanType
            }.count { it > 30 }
            method {
                param(ContextClass)
                returnType = BooleanType
            }.count(6)
            method {
                param(StringClass)
                returnType = BooleanType
            }.count(2)
        }.get()?.hook {
            injectMember {
                method {
                    param(StringClass)
                    returnType = BooleanType
                }.all()
                beforeHook {
                    when (args().first().string()) {
                        //Source Iris5SettingsFragment -> iris5_motion_fluency_optimization_switch
                        "oplus.software.video.rm_memc" -> if (memcVideo) resultFalse()
                        "oplus.software.display.pixelworks_enable" -> if (memcVideo) resultTrue()
                    }
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> HookSettingsFeature")
    }
}