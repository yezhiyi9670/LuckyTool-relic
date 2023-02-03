package com.luckyzyx.luckytool.hook.hooker

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.packageinstaller.AllowReplaceInstall
import com.luckyzyx.luckytool.hook.scope.packageinstaller.RemoveInstallAds
import com.luckyzyx.luckytool.hook.scope.packageinstaller.ReplaseAospInstaller
import com.luckyzyx.luckytool.hook.scope.packageinstaller.SkipApkScan
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
        //跳过安装扫描
        if (prefs(ModulePrefs).getBoolean("skip_apk_scan", false)) {
            loadHooker(SkipApkScan)
        }

        //低/相同版本警告
        if (prefs(ModulePrefs).getBoolean("allow_downgrade_install", false)) {
            loadHooker(AllowReplaceInstall)
        }

        //移除安装完成广告
        if (prefs(ModulePrefs).getBoolean("remove_install_ads", false)) {
            loadHooker(RemoveInstallAds)
        }

        //ColorOS安装器替换为原生安装器
        if (prefs(ModulePrefs).getBoolean("replase_aosp_installer", false)) {
            loadHooker(ReplaseAospInstaller)
        }
    }
}