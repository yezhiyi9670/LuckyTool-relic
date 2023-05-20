package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveSomeVipLimit : YukiBaseHooker() {
    override fun onHook() {
        //network_speed_vip -> oppo_acc

        //Source VipInfoBean -> VipInfosDTO
        //<string name="magic_voice_buy_vip_tip">开启游戏变声，尽享全部变声效果</string>
        findClass("com.oplus.games.account.bean.VipInfoBean\$VipInfosDTO").hook {
            injectMember {
                method { name = "getVip" }
                replaceToTrue()
            }
            injectMember {
                method { name = "getExpiredVip" }
                replaceToFalse()
            }
            injectMember {
                method { name = "getExpireTime" }
                replaceTo("2999-12-31")
            }
            injectMember {
                method { name = "getSign" }
                replaceToTrue()
            }
        }
        //Source TrialVipV2InfoBean
        findClass("com.oplus.games.account.bean.TrialVipInfoBean").hook {
            injectMember {
                method { name = "isHit" }
                replaceToTrue()
            }
        }
        //Source TrialVipV2InfoBean
        findClass("com.oplus.games.account.bean.TrialVipV2InfoBean").hook {
            injectMember {
                method { name = "isDeviceHasTrialQualification" }
                replaceToTrue()
            }
            injectMember {
                method { name = "isHit" }
                replaceToTrue()
            }
        }
        //Source VipAccelearateResponse
        findClass("com.oplus.games.account.bean.VipAccelearateResponse").hook {
            injectMember {
                method { name = "getSuperBooster" }
                replaceToTrue()
            }
            injectMember {
                method { name = "isSuperBooster" }
                replaceToTrue()
            }
        }
        //Source VIPStateBean
        findClass("com.oplus.games.account.bean.VIPStateBean").hook {
            injectMember {
                method { name = "getVipState" }
                replaceTo(5)
            }
            injectMember {
                method { name = "getExpireTime" }
                replaceTo("2999-12-31")
            }
        }
        //Source UserInfo
        findClass("com.coloros.gamespaceui.module.magicvoice.oplus.data.UserInfo").hook {
            injectMember {
                method { name = "getExpireTime" }
                replaceTo("2999-12-31")
            }
            injectMember {
                method { name = "getHasTrialQualifications" }
                replaceToTrue()
            }
            injectMember {
                method { name = "getUserIdentity" }
                replaceTo(3)
            }
        }
    }
}