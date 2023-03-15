package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //屏幕省电,电池健康
        loadHooker(BatteryHiddenEntrance)

        //BatteryHealthFragment
        //max_capacity_data
        //battery_health_obtain_fail -> 获取失败

//        findClass("com.oplus.powermanager.fuelgaue.BatteryHealthDataPreference").hook {
//            injectMember {
//                method {
//                    name = "f"
//                }
//                beforeHook {
//                    val file = File("/sys/class/oplus_chg/battery/battery_fcc")
//                    if (!file.exists() || !file.isFile) return@beforeHook
//                    val context = field {
//                        type = ContextClass
//                    }.get(instance).cast<Context>()
//                    val textView = field {
//                        type = TextViewClass
//                    }.get(instance).cast<TextView>()
//                    textView?.text = loadFile(file)
//                    resultNull()
//                }
//            }
//        }
    }
}