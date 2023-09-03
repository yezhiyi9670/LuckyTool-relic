package com.luckyzyx.luckytool.hook.scope.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookPackageInstallerFeature : YukiBaseHooker() {
    override fun onHook() {
        val isAOSP = false//(ModulePrefs).getBoolean("replase_aosp_installer", false)
        val isAds = prefs(ModulePrefs).getBoolean("remove_install_ads", false)
        //Source FeatureOption
        findClass("com.android.packageinstaller.oplus.common.FeatureOption").hook {
            injectMember {
                method { name = "init";paramCount = 1 }
                afterHook {
                    if (isAds) field { name = "sIsBusinessCustomProduct" }.get().setFalse()
                }
            }
            injectMember {
                method { name = "setIsClosedSuperFirewall";paramCount = 1 }
                afterHook {
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