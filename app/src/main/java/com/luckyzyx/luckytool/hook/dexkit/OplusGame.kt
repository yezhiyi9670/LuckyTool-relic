package com.luckyzyx.luckytool.hook.dexkit

import android.content.Context
import com.luckyzyx.luckytool.hook.scope.oplusgames.CompetitionModeSound
import com.luckyzyx.luckytool.hook.scope.oplusgames.RemoveRootCheck
import com.luckyzyx.luckytool.utils.DexkitUtils.printValue
import com.luckyzyx.luckytool.utils.arraySummaryLine

object OplusGame {

    fun get(context: Context, appPath: String): String {
        return arraySummaryLine(
            RemoveRootCheck.let {
                it.searchDexkit(appPath).printValue(context, "RemoveRootCheck", it.key)
            },
            CompetitionModeSound.let {
                it.searchDexkit(appPath).printValue(context, "CompetitionModeSound", it.key)
            }
        )
    }

}