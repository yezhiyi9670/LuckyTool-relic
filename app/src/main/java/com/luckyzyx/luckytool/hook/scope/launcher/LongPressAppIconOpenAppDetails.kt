package com.luckyzyx.luckytool.hook.scope.launcher

import android.os.UserHandle
import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.openAppDetailIntent

object LongPressAppIconOpenAppDetails : YukiBaseHooker() {
    override fun onHook() {

        fun View.setLongClick(packName: String?, userId: Int?) {
            setOnLongClickListener {
                packName?.let { its -> openAppDetailIntent(it.context, its, userId) }
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
                    }.get(instance).call()
                    val itemInfo = method {
                        name = "getItemInfo"
                    }.get(instance).call()
                    val userHandle = itemInfo?.current()?.field {
                        name = "user"
                        superClass()
                    }?.cast<UserHandle>()
                    val userId = userHandle?.current()?.method {
                        name = "getIdentifier"
                    }?.int()
                    val packName = if (SDK >= A13) {
                        task?.current()?.method {
                            name = "getPackageName"
                        }?.invoke<String>()
                    } else {
                        task?.current()?.field {
                            name = "key"
                        }?.any()?.current()?.method {
                            name = "getPackageName"
                        }?.invoke<String>()
                    }
                    val headerView = method {
                        name = "getHeaderView"
                    }.get(instance).call()
                    val iconView = headerView?.current()?.method {
                        name = "getTaskIcon"
                    }?.invoke<View>()
                    val titleView = headerView?.current()?.let {
                        if (SDK >= A13) {
                            it.method {
                                name = "getTitleTv"
                            }.invoke<TextView>()
                        } else {
                            it.field {
                                name = "mTitleView"
                            }.cast<TextView>()
                        }
                    }
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
                    val userId = key?.current()?.field {
                        name = "userId"
                    }?.int()
                    val packName = if (SDK >= A13) {
                        task?.current()?.method {
                            name = "getPackageName"
                        }?.invoke<String>()
                    } else {
                        task?.current()?.field {
                            name = "key"
                        }?.any()?.current()?.method {
                            name = "getPackageName"
                        }?.invoke<String>()
                    }
                    instance<View>().setLongClick(packName, userId)
                }
            }
        }
    }
}