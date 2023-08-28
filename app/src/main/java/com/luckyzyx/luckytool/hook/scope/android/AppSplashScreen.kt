package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object AppSplashScreen : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("disable_splash_screen", false)
        //Source StartingSurfaceController
        if (SDK > 28) return
        findClass("com.android.server.OplusServiceRegistry").hook {
            injectMember {
                method { name = "addOplusStartingWindowManager" }
                if (isEnable) intercept()
            }
        }

        //Source StartingWindowRUSHelper
        findClass("com.android.server.wm.startingwindow.StartingWindowNameListManager").hook {
            injectMember {
                method {
                    param(StringClass)
                    returnType = BooleanType
                }.all()
                afterHook {
                    val key = args().first().string()
                    loggerD(msg = "StartingWindowNameListManager ${method.name} ($key) -> $result")
                }
            }
        }

        findClass("com.android.server.wm.startingwindow.StartingWindowUtils").hook {
            injectMember {
                method {
                    param(StringClass)
                    returnType = BooleanType
                }.all()
                afterHook {
                    val key = args().first().string()
                    loggerD(msg = "StartingWindowUtils ${method.name} ($key) -> $result")
                }
            }
        }

        findClass("com.android.server.wm.OplusStartingWindowManager").hook {
            injectMember {
                method {
                    param(StringClass)
                    returnType = BooleanType
                }.all()
                afterHook {
                    val key = args().first().string()
                    loggerD(msg = "OplusStartingWindowManager ${method.name} ($key) -> $result")
                }
            }
        }
    }
}