package com.luckyzyx.luckytool.hook.scope.weather

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveWeatherAD : YukiBaseHooker() {
    override fun onHook() {
        "com.oplus.weather.utils.LocalUtils".toClass().apply {
            method { name = "startBrowserForUrl" }.hook {
                before {
                    args(0).set(0)
                    args(2).set("${args[2]}&infoEnable=false")
                    args().last().set(true)
                }
            }
            method { name = "jumpToBrowser" }.hook() {
                before {
                    args(2).set("${args[2]}&infoEnable=false")
                }
            }
        }
    }
}