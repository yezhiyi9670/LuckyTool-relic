package com.luckyzyx.luckytool.hook.scope.oplusota

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object UnlockLocalUpgrade : YukiBaseHooker() {
    override fun onHook() {
        //Source EntryActivity
        findClass("com.oplus.otaui.activity.EntryActivity").hook {
            injectMember {
                method {
                    name = "onCreateOptionsMenu"
                    paramCount = 1
                }
                afterHook {
                    val instance = instance<Activity>()
                    val menu = args(0).cast<Menu>()!!
                    if (menu.size() < 3) return@afterHook
                    field {
                        type(MenuItem::class.java).index(2)
                    }.get(instance).cast<MenuItem>()?.apply {
                        if (!isEnabled) {
                            isEnabled = true
                            setOnMenuItemClickListener {
                                instance.startActivityForResult(
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
}