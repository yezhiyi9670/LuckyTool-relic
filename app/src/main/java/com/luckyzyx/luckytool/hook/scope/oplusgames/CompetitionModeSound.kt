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
import com.luckyzyx.luckytool.utils.DexkitPrefs
import com.luckyzyx.luckytool.utils.ModulePrefs
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object CompetitionModeSound : YukiBaseHooker() {
    const val key = "remove_competition_mode_sound"
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_competition_mode_sound", false)
        if (!isEnable) return
        val clsName = prefs(DexkitPrefs).getString(RemoveRootCheck.key, "null")

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

    fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexKitBridge.create(appPath)?.use { bridge ->
            result = bridge.findClass {
                searchPackages = listOf(
                    "v9", "w9", "u9", "n9", "m9", "ve", "pe", "x9", "y9", "ca", "ea", "la", "q8",
                    "m8", "ja"
                )
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