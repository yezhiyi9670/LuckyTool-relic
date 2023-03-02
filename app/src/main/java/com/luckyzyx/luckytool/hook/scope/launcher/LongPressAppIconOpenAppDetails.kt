package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.data.openAppDetailIntent

object LongPressAppIconOpenAppDetails : YukiBaseHooker() {
    override fun onHook() {

        fun View.setLongClick(packName: String?) {
            setOnLongClickListener {
                packName?.let { its -> openAppDetailIntent(it.context, its) }
                true
            }
        }

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
                    }.get(instance).invoke<Any>()
                    val packName = task?.current()?.method {
                        name = "getPackageName"
                    }?.invoke<String>()
                    val headerView = method {
                        name = "getHeaderView"
                    }.get(instance).invoke<Any>()
                    val iconView = headerView?.current()?.method {
                        name = "getTaskIcon"
                    }?.invoke<View>()
                    val titleView = headerView?.current()?.method {
                        name = "getTitleTv"
                    }?.invoke<TextView>()
                    iconView?.setLongClick(packName)
                    titleView?.setLongClick(packName)
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
                    }.get(instance).invoke<Any>()
                    val packName = task?.current()?.method {
                        name = "getPackageName"
                    }?.invoke<String>()
                    instance<View>().setLongClick(packName)
                }
            }
        }
    }
}