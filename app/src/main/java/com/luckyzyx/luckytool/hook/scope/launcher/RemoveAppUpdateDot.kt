package com.luckyzyx.luckytool.hook.scope.launcher

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveAppUpdateDot : YukiBaseHooker() {
    override fun onHook() {
        val clazz = if (SDK >= A13) "com.android.launcher3.BubbleTextView" //C13 C14
        else if (SDK >= A12) "com.android.launcher3.OplusBubbleTextView" //C12.1
        else "com.android.launcher3.OplusBubbleTextView" //A11 C12.0
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
                        name = "title";superClass()
                    }.cast<CharSequence>()
                    instance<TextView>().text = title
                    resultNull()
                }
            }
        }
    }
}