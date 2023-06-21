package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveSettingsBottomLaboratory : YukiBaseHooker() {
    override fun onHook() {
        //Source TopLevelLaboratoryPreferenceController
        findClass("com.oplus.settings.feature.homepage.TopLevelLaboratoryPreferenceController").hook {
            injectMember {
                method {
                    name = "getAvailabilityStatus"
                }
                replaceTo(3)
            }
        }
    }
}