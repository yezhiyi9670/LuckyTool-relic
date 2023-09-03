package com.luckyzyx.luckytool.hook.scope.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import java.util.*

class SkipApkScan(private val commit: String) : YukiBaseHooker() {

    @Suppress("LocalVariableName")
    override fun onHook() {
        val OPIA = "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity"
        val ADRU = "com.android.packageinstaller.oplus.utils.AppDetailRedirectionUtils"
        val isNew = ADRU.toClass().hasMethod { name = "shouldStartAppDetail" }
        val member: Array<String> =
            when (commit) {
                "7bc7db7", "e1a2c58" -> arrayOf(OPIA, "L", "C", "K")
                "75fe984", "532ffef" -> arrayOf(OPIA, "L", "D", "i")
                "38477f0" -> arrayOf(OPIA, "M", "D", "k")
                "a222497" -> arrayOf(OPIA, "M", "E", "j")
                else -> if (isNew)
                    arrayOf(ADRU, "shouldStartAppDetail", "checkToScanRisk", "initiateInstall")
                else arrayOf(OPIA, "isStartAppDetail", "checkToScanRisk", "initiateInstall")
            }
        //Source OPlusPackageInstallerActivity ? AppDetailRedirectionUtils
        //Search SP_KEY_COUNT_CANCELED_BY_APP_DETAIL / count_canceled_by_app_detail
        findClass(member[0]).hook {
            injectMember {
                method {
                    name = member[1]
                    if (member[0] == OPIA) returnType = BooleanType
                    if (member[0] == ADRU) returnType = IntType
                }.all()
                if (member[0] == OPIA) replaceToFalse()
                if (member[0] == ADRU) replaceTo(9)
            }
        }
        //Source OPlusPackageInstallerActivity
        //Search button_type / install_old_version_button
        findClass(OPIA).hook {
            injectMember {
                method { name = member[2] }
                replaceUnit { method { name = member[3] }.get(instance).call() }
            }
        }
    }
}
