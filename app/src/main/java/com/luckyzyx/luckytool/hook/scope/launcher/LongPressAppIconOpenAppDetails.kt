package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.openAppDetailIntent

object LongPressAppIconOpenAppDetails : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusTaskHeaderView
        findClass("com.android.quickstep.views.OplusTaskViewImpl").hook {
            injectMember {
                method {
                    name = "setIcon"
                    paramCount(1..2)
                }
                afterHook {
                    val task = method {
                        name = "getTask"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).call()
                    val key = task?.current()?.field {
                        name = "key"
                    }?.any()
                    val packName = key?.current()?.method {
                        name = "getPackageName"
                    }?.invoke<String>()
                    val userId = key?.current()?.field {
                        name = "userId"
                    }?.int()
                    val headerView = method {
                        name = "getHeaderView"
                    }.get(instance).call()
                    val iconView = headerView?.current()?.method {
                        name = "getTaskIcon"
                    }?.invoke<View>()
                    val titleView = headerView?.current()?.field {
                        name = if (SDK >= A13) "titleTv" else "mTitleView"
                    }?.cast<TextView>()
                    iconView?.setLongClick(packName, userId)
                    titleView?.setLongClick(packName, userId)
                }
            }
        }

        //Source DockIconView
        findClass("com.oplus.quickstep.dock.DockIconView").hook {
            injectMember {
                method {
                    name = "setIcon"
                    paramCount = 1
                }
                afterHook {
                    val task = method {
                        name = "getTask"
                    }.get(instance).call()
                    val key = task?.current()?.field {
                        name = "key"
                    }?.any()
                    val packName = key?.current()?.method {
                        name = "getPackageName"
                    }?.invoke<String>()
                    val userId = key?.current()?.field {
                        name = "userId"
                    }?.int()
                    instance<View>().setLongClick(packName, userId)
                }
            }
        }
    }

    private fun View.setLongClick(packName: String?, userId: Int? = 0) {
        setOnLongClickListener {
            packName?.let { its -> it.context.openAppDetailIntent(its, userId) }
            true
        }
    }
}