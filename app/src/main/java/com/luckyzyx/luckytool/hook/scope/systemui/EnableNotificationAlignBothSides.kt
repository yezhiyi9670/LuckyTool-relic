package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Resources
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.utils.tools.safeOf

object EnableNotificationAlignBothSides : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source BasePanelViewController
        findClass("com.oplusos.systemui.statusbar.phone.BasePanelViewController").hook {
            injectMember {
                method {
                    name = "initResource"
                    paramCount = 1
                }
                afterHook {
                    val res = args().first().cast<Resources>() ?: return@afterHook
                    val qsPanelPadding = safeOf(null) {
                        res.getDimensionPixelSize(
                            res.getIdentifier(
                                "qs_header_panel_side_padding", "dimen", packageName
                            )
                        )
                    } ?: run {
                        loggerD(msg = "$packageName\nError -> EnableNotificationLeftAndRightAlignment")
                        return@afterHook
                    }
                    method {
                        name = "setHorizontalMarginStackLayout"
                        paramCount = 1
                    }.get(instance).call(qsPanelPadding)
                }
            }
        }
    }
}