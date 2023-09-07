package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ForceDisplayProcessManagement : YukiBaseHooker() {
    override fun onHook() {
        //com.oplus.settings.feature.process.RunningApplicationActivity
        //Source RunningApplicationsPreferenceController
        findClass("com.oplus.settings.feature.othersettings.controller.RunningApplicationsPreferenceController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
        //Source RunningApplicationsNewPreferenceController
        findClass("com.oplus.settings.feature.appmanager.controller.RunningApplicationsNewPreferenceController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
    }
}