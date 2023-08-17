package com.luckyzyx.luckytool.hook.statusbar

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.getOSVersionCode
import com.luckyzyx.luckytool.utils.getScreenOrientation

@Suppress("UNUSED_VARIABLE", "DiscouragedApi")
object StatusBarLayout : YukiBaseHooker() {
    private var statusBarLeftMargin: Int = 0
    private var statusBarRightMargin: Int = 0
    private var statusBarTopMargin: Int = 0
    private var statusBarBottomMargin: Int = 0

    override fun onHook() {
        if (SDK < A13) return
        var mLeftLayout: LinearLayout? = null
        var mRightLayout: LinearLayout? = null
        var mCenterLayout: LinearLayout?
        var mStatusBar: ViewGroup? = null

        val layoutMode = prefs(ModulePrefs).getString("statusbar_layout_mode", "0")

        val isCompatibleMode =
            prefs(ModulePrefs).getBoolean("statusbar_layout_compatible_mode", false)
        val leftMargin =
            prefs(ModulePrefs).getInt("statusbar_layout_left_margin", 0)
        val rightMargin =
            prefs(ModulePrefs).getInt("statusbar_layout_right_margin", 0)

        fun updateCustomLayout(context: Context) {
            getScreenOrientation(context) {
                if (it) {
                    mLeftLayout?.setPadding(statusBarLeftMargin, 0, 0, 0)
                    mRightLayout?.setPadding(0, 0, statusBarRightMargin, 0)
                    mStatusBar?.setPadding(0, statusBarTopMargin, 0, statusBarBottomMargin)
                } else {
                    mLeftLayout?.setPadding(0, 0, 0, 0)
                    mRightLayout?.setPadding(0, 0, 0, 0)
                }
            }
        }

        fun updateDefaultLayout(context: Context, leftView: ViewGroup?, rightView: ViewGroup?) {
            if (!isCompatibleMode) return
            getScreenOrientation(context) {
                if (it) {
                    leftView?.setPadding(leftMargin, 0, 0, 0)
                    rightView?.setPadding(0, 0, rightMargin, 0)
                } else {
                    leftView?.setPadding(0, 0, 0, 0)
                    rightView?.setPadding(0, 0, 0, 0)
                }
            }
        }

        fun setCustomMargin() {
            if (!isCompatibleMode) return
            if (leftMargin != 0) statusBarLeftMargin = leftMargin
            if (rightMargin != 0) statusBarRightMargin = rightMargin
        }

        //Source ScreenDecorations
        findClass("com.android.systemui.ScreenDecorations\$DisplayCutoutView").hook {
            injectMember {
                method {
                    name = "boundsFromDirection"
                    paramCount = 3
                }
                beforeHook {
                    if (isCompatibleMode) args(1).set(0)
                }
            }
        }

        //Source CollapsedStatusBarFragment
        VariousClass(
            "com.android.systemui.statusbar.phone.CollapsedStatusBarFragment", //A12
            "com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment" //C13
        ).hook {
            injectMember {
                method {
                    name = "onViewCreated"
                    paramCount = 2
                }
                afterHook {
                    val phoneStatusBarView = args(0).cast<ViewGroup>()!!
                    val context = phoneStatusBarView.context
                    val res = phoneStatusBarView.resources
                    val statusBarId = res?.getIdentifier("status_bar", "id", packageName)
                    val statusBarContentsId =
                        res?.getIdentifier("status_bar_contents", "id", packageName)

                    val statusBarLeftSideId =
                        res?.getIdentifier("status_bar_left_side", "id", packageName)
                    val clockId = res?.getIdentifier("clock", "id", packageName)
                    val systemPromptViewId =
                        res?.getIdentifier("system_prompt_view", "id", packageName)
                    val notificationIconAreaInnerId =
                        res?.getIdentifier("notification_icon_area_inner", "id", packageName)

                    val systemIconAreaId = res?.getIdentifier("system_icon_area", "id", packageName)
                    val statusIconsId = res?.getIdentifier("statusIcons", "id", packageName)
                    val batteryId = res?.getIdentifier("battery", "id", packageName)

                    mStatusBar =
                        statusBarId?.let { phoneStatusBarView.findViewById(it) } ?: return@afterHook
                    val statusBarContents: ViewGroup? =
                        statusBarContentsId?.let { phoneStatusBarView.findViewById(it) }
                    val statusBarLeftSide: ViewGroup? =
                        statusBarLeftSideId?.let { phoneStatusBarView.findViewById(it) }
                    val clock: TextView? = clockId?.let { phoneStatusBarView.findViewById(it) }
                    val systemPromptView: ImageView? =
                        systemPromptViewId?.let { phoneStatusBarView.findViewById(it) }
                    val notificationIconAreaInner: ViewGroup? =
                        notificationIconAreaInnerId?.let { phoneStatusBarView.findViewById(it) }
                    val systemIconArea: ViewGroup? =
                        systemIconAreaId?.let { phoneStatusBarView.findViewById(it) }
                    val statusIcons: ViewGroup? =
                        statusIconsId?.let { phoneStatusBarView.findViewById(it) }
                    val battery: ViewGroup? =
                        batteryId?.let { phoneStatusBarView.findViewById(it) }

                    mLeftLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                        )
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    }

                    mCenterLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
                    }

                    mRightLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                        )
                        gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    }

                    if (layoutMode == "0" && isCompatibleMode) {
                        setCustomMargin()
                        updateDefaultLayout(context, statusBarLeftSide, systemIconArea)
                        return@afterHook
                    }
                    if (layoutMode.isBlank() || layoutMode == "0") return@afterHook

                    (clock?.parent as ViewGroup).removeView(clock)
                    (statusBarLeftSide?.parent as ViewGroup).removeView(statusBarLeftSide)
                    (systemIconArea?.parent as ViewGroup).removeAllViews()

                    statusBarLeftSide.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    mLeftLayout?.addView(statusBarLeftSide)
                    mCenterLayout?.addView(clock)
                    mRightLayout?.addView(systemIconArea)

                    statusBarContents?.addView(mLeftLayout, 0)
                    statusBarContents?.addView(mCenterLayout)
                    statusBarContents?.addView(mRightLayout)

                    statusBarLeftMargin = mStatusBar?.paddingLeft!!
                    statusBarRightMargin = mStatusBar?.paddingRight!!
                    statusBarTopMargin = mStatusBar?.paddingTop!!
                    statusBarBottomMargin = mStatusBar?.paddingBottom!!

                    setCustomMargin()
                    updateCustomLayout(context)
                }
            }
            injectMember {
                method { name = "onDestroyView" }
                if (layoutMode != "0" && getOSVersionCode == 26) intercept()
            }
        }

        //Source PhoneStatusBarView
        findClass("com.android.systemui.statusbar.phone.PhoneStatusBarView").hook {
            injectMember {
                method { name = "updateLayoutForCutout" }
                afterHook {
                    if (isCompatibleMode) updateCustomLayout(instance<ViewGroup>().context)
                }
            }
        }

        //Source KeyguardStatusBarViewExImpl
        VariousClass(
            "com.oplusos.systemui.statusbar.phone.KeyguardStatusBarViewEx", //A12
            "com.oplus.systemui.statusbar.phone.KeyguardStatusBarViewExImpl"
        ).hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook {
                    //keyguard_status_bar_contents
                    if (isCompatibleMode) field {
                        name = "keyguardStatusbarLeftContView"
                    }.get(instance).cast<ViewGroup>()?.setPadding(leftMargin, 0, 0, 0)
                }
            }
        }
    }
}