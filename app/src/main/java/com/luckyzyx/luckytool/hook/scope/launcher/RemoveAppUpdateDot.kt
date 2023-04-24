package com.luckyzyx.luckytool.hook.scope.launcher

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A121
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveAppUpdateDot : YukiBaseHooker() {
    override fun onHook() {
        val clazz = when (SDK) {
            A13 -> "com.android.launcher3.BubbleTextView"
            A12, A121 -> "com.android.launcher3.OplusBubbleTextView"
            else -> "com.android.launcher3.BubbleTextView"
        }
        //Source OplusBubbleTextView
        findClass(clazz).hook {
            injectMember {
                method {
                    name = "applyLabel"
                    paramCount = when (instanceClass.simpleName) {
                        "BubbleTextView" -> 1
                        "OplusBubbleTextView" -> 3
                        else -> 1
                    }
                }
                beforeHook {
                    val itemInfoWithIcon = args().first().any() ?: return@beforeHook
                    val title = itemInfoWithIcon.current().field {
                        name = "title"
                        superClass()
                    }.cast<CharSequence>()
                    instance<TextView>().text = title
                    resultNull()
                }
            }
        }
    }
}