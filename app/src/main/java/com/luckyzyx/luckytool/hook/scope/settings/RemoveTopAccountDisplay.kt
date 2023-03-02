package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object RemoveTopAccountDisplay : YukiBaseHooker() {
    override fun onHook() {
        //Source UserPreferenceController
        findClass("com.oplus.settings.feature.homepage.user.UserPreferenceController").hook {
            injectMember {
                method {
                    name = "displayPreference"
                    paramCount = 1
                }
                afterHook {
                    val preferenceScreen = args().first().any()
                    val key = method {
                        name = "getPreferenceKey"
                        superClass()
                    }.get(instance).string()
                    preferenceScreen?.current {
                        method {
                            name = "removePreferenceRecursively"
                            superClass()
                        }.call(key)
                    }
                }
            }
        }
    }
}