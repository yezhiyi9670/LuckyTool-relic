package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils.ckqcbss
import com.luckyzyx.luckytool.utils.callFunc

@Suppress("DEPRECATION")
class AliveActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 设置透明窗口 */
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window?.statusBarColor = getColor(R.color.transparent)
        window?.navigationBarColor = getColor(R.color.transparent)
        ckqcbss()
        callFunc(intent.extras)
        finish()
    }
}