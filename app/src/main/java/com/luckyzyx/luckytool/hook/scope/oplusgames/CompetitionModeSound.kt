package com.luckyzyx.luckytool.hook.scope.oplusgames

import android.media.AudioManager
import android.media.SoundPool
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.SparseIntArrayClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import org.luckypray.dexkit.query.ClassDataList

object CompetitionModeSound : YukiBaseHooker() {
    const val key = "remove_competition_mode_sound"
    override fun onHook() {
        val clsName = searchDexkit(appInfo.sourceDir).firstOrNull()?.className
            ?: "null"
        //Source SoundPoolPlayManager -> competition_mode_sound
        findClass(clsName).hook {
            injectMember {
                method {
                    param(IntType)
                    paramCount = 1
                }.all()
                beforeHook {
                    loggerD(msg = args().first().int().toString())
                    if (args().first().int() == 9) resultNull()
                }
            }
        }
    }

    private fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexkitUtils.create(appPath)?.use { bridge ->
            result = bridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(BooleanType.name)
                        addForType(SoundPool::class.java.name)
                        addForType(AudioManager::class.java.name)
                        addForType(SparseIntArrayClass.name)
                    }
                    methods {
                        add {
                            paramCount(0)
                            returnType(UnitType.name)
                        }
                        add {
                            paramTypes(IntType.name)
                            returnType(UnitType.name)
                        }
                    }
                }
            }
        }
        return result
    }
}