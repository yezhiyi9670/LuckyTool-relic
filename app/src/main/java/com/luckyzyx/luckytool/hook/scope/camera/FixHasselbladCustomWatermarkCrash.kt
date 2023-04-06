package com.luckyzyx.luckytool.hook.scope.camera

import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.hasMethod

object FixHasselbladCustomWatermarkCrash : YukiBaseHooker() {
    override fun onHook() {
        var pictureResult: Any? = null
        //Source CameraPictureCallbackAdapter
        findClass("com.oplus.ocs.camera.CameraPictureCallbackAdapterV2").hook {
            injectMember {
                method {
                    name = "onFinishAddFrame"
                    paramCount = 1
                }
                beforeHook {
                    pictureResult = args().first().any()
                }
            }
        }
        //Source CameraPictureCallbackAdapter
        findClass("com.oplus.ocs.camera.CameraPictureCallbackAdapterV2$1").hook {
            injectMember {
                method {
                    name = "updateWatermark"
                    paramCount = 1
                }
                beforeHook {
                    val bundle = args().first().cast<Bundle>()
                    val hasCallbackAdapter = pictureResult?.javaClass?.hasMethod {
                        name = "getCameraPictureResultCallbackAdapter"
                    }
                    if (hasCallbackAdapter == true) return@beforeHook
                    pictureResult?.javaClass?.hasMethod {
                        name = "getCameraPictureCallback"
                    } ?: return@beforeHook
                    val getCameraPictureCallback = pictureResult?.current(ignored = true)?.method {
                        name = "getCameraPictureCallback"
                    }?.call()
                    getCameraPictureCallback?.current()?.method {
                        name = "updateWatermark"
                    }?.call(bundle)
                    resultNull()
                }
            }
        }
    }
}