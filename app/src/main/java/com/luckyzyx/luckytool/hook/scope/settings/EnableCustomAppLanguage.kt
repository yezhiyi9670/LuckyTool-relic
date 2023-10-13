package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object EnableCustomAppLanguage : YukiBaseHooker() {
    override fun onHook() {
        //Source AppLocaleUtil
        "com.android.settings.applications.AppLocaleUtil".toClass().apply {
            method { name = "canDisplayLocaleUi" }.hook {
                replaceToTrue()
            }
        }
        //Source AppLocalePreferenceController
        "com.android.settings.applications.appinfo.AppLocalePreferenceController".toClass().apply {
            method { name = "getAvailabilityStatus" }.hook {
                replaceTo(0)
            }
        }
    }
}