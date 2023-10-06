package com.luckyzyx.luckytool.hook.scope.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookPackageInstallerFeature : YukiBaseHooker() {
    override fun onHook() {
        val isAOSP = false//(ModulePrefs).getBoolean("replase_aosp_installer", false)
        val isAds = prefs(ModulePrefs).getBoolean("remove_install_ads", false)
        //Source FeatureOption
        "com.android.packageinstaller.oplus.common.FeatureOption".toClass().apply {
            method { name = "init";paramCount = 1 }.hook {
                after {
                    if (isAds) field { name = "sIsBusinessCustomProduct" }.get().setFalse()
                }
            }
            method { name = "setIsClosedSuperFirewall";paramCount = 1 }.hook {
                after {
                    if (isAOSP) field { name = "sIsClosedSuperFirewall" }.get().setTrue()
                }
            }
        }

        //search class -> DeleteStagedFileOnResult
        //search class.method -> onCreate +4 -> class.method
//        findClass("com.android.packageinstaller.DeleteStagedFileOnResult").hook {
//            injectMember {
//                method {
//                    name = "onCreate"
//                    param(BundleClass)
//                }
//                beforeHook {
//                    args(0).setNull()
//                    VariousClass(
//                        "com.android.packageinstaller.oplus.common.j",
//                        "com.android.packageinstaller.oplus.common.FeatureOption"
//                    ).toClass().field {
//                        type(BooleanType).index(1)
//                    }.get().setTrue()
//                }
//            }
//        }
    }
}