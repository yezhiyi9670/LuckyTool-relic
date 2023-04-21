package com.luckyzyx.luckytool.hook.scope.packageinstaller

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object RemoveInstallAds : YukiBaseHooker() {
    private var ins: Any? = null
    override fun onHook() {
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress").hook {
            injectMember {
                method {
                    name = "initView"
                }
                afterHook {
                    ins = instance
                    removeViews()
                }
            }
        }
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress$1").hook {
            injectMember {
                method {
                    name = "handleMessage"
                }
                afterHook { removeViews() }
            }
        }
    }

    private fun removeViews() {
        ins?.current()?.field {
            name = "mSuggestLayoutAScrollView"
        }?.cast<View>()?.isVisible = false
        ins?.current()?.field {
            name = "mSuggestLayoutB"
        }?.cast<View>()?.isVisible = false
    }
}