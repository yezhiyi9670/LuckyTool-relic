package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.BluetoothIconRelated
import com.luckyzyx.luckytool.hook.scope.systemui.MobileDataIconRelated
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveGreenCapsulePrompt
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveGreenDotPrivacyPrompt
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveHighPerformanceModeIcon
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveStatusBarSecurePayment
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveWiFiDataInout
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarIconVerticalCenter
import com.luckyzyx.luckytool.utils.ModulePrefs

object StatusBarIcon : YukiBaseHooker() {
    override fun onHook() {
        //移除状态栏支付保护图标
        if (prefs(ModulePrefs).getBoolean("remove_statusbar_securepayment_icon", false)) {
            loadHooker(RemoveStatusBarSecurePayment)
        }
        //移除WiFi数据箭头
        if (prefs(ModulePrefs).getBoolean("remove_wifi_data_inout", false)) {
            loadHooker(RemoveWiFiDataInout)
        }
        //移动数据图标相关
        loadHooker(MobileDataIconRelated)
        //未连接蓝牙时隐藏图标
        loadHooker(BluetoothIconRelated)
        //移除高性能模式图标
        if (prefs(ModulePrefs).getBoolean("remove_high_performance_mode_icon", false)) {
            loadHooker(RemoveHighPerformanceModeIcon)
        }
        //移除绿点隐私提示
        if (prefs(ModulePrefs).getBoolean("remove_green_dot_privacy_prompt", false)) {
            loadHooker(RemoveGreenDotPrivacyPrompt)
        }
        //移除绿色胶囊提示
        if (prefs(ModulePrefs).getBoolean("remove_green_capsule_prompt", false)) {
            loadHooker(RemoveGreenCapsulePrompt)
        }
        //状态栏图标垂直居中
        if (prefs(ModulePrefs).getBoolean("status_bar_icon_vertical_center", false)) {
            loadHooker(StatusBarIconVerticalCenter)
        }
    }
}