package com.luckyzyx.luckytool.hook.scope.oplusota

import android.app.Activity
import android.content.Intent
import android.view.Menu
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class UnlockLocalUpgrade : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.otaui.activity.EntryActivity").hook {
            injectMember {
                method {
                    name = "onCreateOptionsMenu"
                    paramCount = 1
                }
                afterHook {
                    val context = instance<Activity>()
                    val menu = args(0).cast<Menu>()!!
                    val localId = menu.getItem(2).itemId
                    menu.findItem(localId)?.apply {
                        isEnabled = true
                        setOnMenuItemClickListener {
                            context.startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(
                                    Intent.CATEGORY_OPENABLE
                                ).setType("*/*"), 100
                            )
                            true
                        }
                    }
                }
            }
        }
    }
}