package com.luckyzyx.luckytool.hook.scope.pictorial

import android.widget.LinearLayout
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType

object RemoveVideoSaveWaterMark : YukiBaseHooker() {
    override fun onHook() {
        //Source VideoWaterMarkView -> view_video_water_mark
        findClass("com.heytap.pictorial.data.VideoWaterMarkView").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook { instance<LinearLayout>().removeAllViews() }
            }
        }
    }
}