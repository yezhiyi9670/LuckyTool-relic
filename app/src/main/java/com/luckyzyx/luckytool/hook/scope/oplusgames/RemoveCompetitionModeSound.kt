package com.luckyzyx.luckytool.hook.scope.oplusgames

import android.media.AudioManager
import android.media.SoundPool
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.SparseIntArrayClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType

object RemoveCompetitionModeSound : YukiBaseHooker() {
    override fun onHook() {
        //Source SoundPoolPlayManager -> competition_mode_sound
        searchClass {
            from("v9", "w9", "u9", "n9", "m9", "ve", "pe").absolute()
            field {
                type = ContextClass
            }.count(1)
            field {
                type = AudioManager::class.java
            }.count(1)
            field {
                type = SoundPool::class.java
            }.count(1)
            field {
                type = SparseIntArrayClass
            }.count(2)
            field {
                type = BooleanType
            }.count(2)
            method {
                param(IntType)
                paramCount = 1
            }.count(2)
        }.get()?.hook {
            injectMember {
                method {
                    param(IntType)
                    paramCount = 1
                }.all()
                beforeHook {
                    if (args().first().int() == 9) resultNull()
                }
            }
        }
    }
}