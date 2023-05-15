package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import kotlin.math.abs

object DoubleClickLockScreen : YukiBaseHooker() {
    override fun onHook() {
        var curTouchTime = 0L
        var curTouchX = 0F
        var curTouchY = 0F

        //Source PhoneStatusBarView
        findClass("com.android.systemui.statusbar.phone.PhoneStatusBarView").hook {
            injectMember {
                method { name = "onFinishInflate" }
                beforeHook {
                    val statusbar = instance<ViewGroup>()
                    statusbar.setOnTouchListener { v, event ->
                        if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
                        val lastTouchTime = curTouchTime
                        val lastTouchX = curTouchX
                        val lastTouchY = curTouchY
                        curTouchTime = System.currentTimeMillis()
                        curTouchX = event.x
                        curTouchY = event.y
                        if (curTouchTime - lastTouchTime < 250L && abs(curTouchX - lastTouchX) < 100f && abs(
                                curTouchY - lastTouchY
                            ) < 100f
                        ) {
                            goToSleep(v.context)
                            curTouchTime = 0L
                            curTouchX = 0F
                            curTouchY = 0F
                        }
                        v.performClick()
                        false
                    }
                }
            }
        }
    }

    private fun goToSleep(context: Context) {
        val service = context.getSystemService(Context.POWER_SERVICE)
        service.current().method { name = "goToSleep" }.call(SystemClock.uptimeMillis())
    }
}