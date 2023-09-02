package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RestorePageLayoutRowCountForEditTiles : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusQSCustomizer
        findClass("com.oplusos.systemui.qs.customize.OplusQSCustomizer").hook {
            injectMember {
                constructor { paramCount = 2 }
                afterHook {
                    field { name = "mMoreFunctionLabel" }.get(instance).cast<View>()
                        ?.isVisible = false
                }
            }
            injectMember {
                method { name = "updateResources" }
                afterHook {
                    field { name = "mRecyclerViewTop" }.get(instance).cast<View>()?.apply {
                        layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                            height = (height / 3) * 4
                        }
                    }
                }
            }
        }
    }
}