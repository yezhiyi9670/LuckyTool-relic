package com.luckyzyx.luckytool.hook.hooker

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.packageinstaller.*
import com.luckyzyx.luckytool.utils.tools.ModulePrefs
import java.util.*

object HookPackageInstaller : YukiBaseHooker() {
    override fun onHook() {
        val appSet =
            prefs(ModulePrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
                Arrays.sort(this)
                forEach {
                    this[this.indexOf(it)] = it.substring(2)
                }
            }
        //非ColorOS官方安装器直接返回
        if (appSet[2] == "null") return
        //HookFeatureOption
        loadHooker(HookFeatureOption)
        //跳过安装扫描
        if (prefs(ModulePrefs).getBoolean("skip_apk_scan", false)) {
            loadHooker(SkipApkScan)
        }
        //低/相同版本警告
        if (prefs(ModulePrefs).getBoolean("allow_downgrade_install", false)) {
            loadHooker(AllowReplaceInstall)
        }
        //显示包名与旧版本号
        if (prefs(ModulePrefs).getBoolean("show_packagename_and_versioncode", false)) {
            loadHooker(ShowPackageNameAndVersionCode)
        }
        //移除安装完成广告
        if (prefs(ModulePrefs).getBoolean("remove_install_ads", false)) {
            loadHooker(RemoveInstallAds)
        }
    }
}