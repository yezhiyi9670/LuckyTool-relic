package com.luckyzyx.luckytool.hook.scope.oplusgames

import android.app.Activity
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object EnableDeveloperPage : YukiBaseHooker() {
    override fun onHook() {
        //Source GameDevelopOptionsActivity
        "business.compact.activity.GameDevelopOptionsActivity".toClass().apply {
            method {
                name = "onCreate"
                paramCount = 1
            }.hook {
                before {
                    instance<Activity>().intent.apply {
                        putExtra("gameDevelopOptions", simpleName)
                        putExtra("openAutomation", -1)
                    }
                    args().first().setNull()
                }
            }
        }
    }
}