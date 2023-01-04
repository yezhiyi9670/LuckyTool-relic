package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.*
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarIcon : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除状态栏电量百分号
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_battery_percent", false)
            ) loadHooker(
                RemoveStatusBarBatteryPercent()
            )
            //移除状态栏支付保护图标
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_securepayment_icon", false)) {
                loadHooker(RemoveStatusBarSecurePayment())
            }
            //移除WiFi数据箭头
            if (prefs(XposedPrefs).getBoolean("remove_wifi_data_inout", false)) {
                loadHooker(RemoveWiFiDataInout())
            }
            //移动数据图标相关
            if (prefs(XposedPrefs).getBoolean("remove_mobile_data_icon", false) || prefs(XposedPrefs).getBoolean("remove_mobile_data_inout", false)) {
                loadHooker(RemoveMobileDataIcon())
            }
            //移除高性能模式图标
            if (prefs(XposedPrefs).getBoolean("remove_high_performance_mode_icon",false)){
                loadHooker(RemoveHighPerformanceModeIcon())
            }
            //状态栏图标垂直居中
            if (prefs(XposedPrefs).getBoolean("status_bar_icon_vertical_center", false)) {
                loadHooker(StatusBarIconVerticalCenter())
            }
        }
    }
}