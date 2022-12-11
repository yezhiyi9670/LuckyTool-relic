package com.luckyzyx.luckytool.utils.data

import java.io.Serializable

data class DonateInfo(
    val name: String,
    val money: Double
) : Serializable

object DonateData {
    fun getDonateList(): ArrayList<DonateInfo> {
        return ArrayList<DonateInfo>().apply {
            add(DonateInfo("是小奶糖啊", 21.0))
            add(DonateInfo("Kimjaejiang", 5.0))
            add(DonateInfo("午时已到", 20.0))
            add(DonateInfo("邹王", 15.0 + 51.66))
            add(DonateInfo("す", 20.0))
            add(DonateInfo("楠", 10.0))
            add(DonateInfo("天伞桜", 88.8))
            add(DonateInfo("北风是不是冷", 15.0))
            add(DonateInfo("ssd.风格", 5.0))
            add(DonateInfo("智", 15.0))
            add(DonateInfo("松花蛋", 10.0))
            add(DonateInfo("佘樂", 20.0))
            add(DonateInfo("风冷涂的蜡", 10.0))
            add(DonateInfo("才", 10.0))
            add(DonateInfo("灯", 5.0))
            add(DonateInfo("G", 66.0))
            add(DonateInfo("荣", 8.80))
            add(DonateInfo("斯已", 15.0))
            add(DonateInfo("冰梦", 20.0))
            add(DonateInfo("?", 8.88))
            add(DonateInfo("晨钟酱", 100.0))
            add(DonateInfo("小李.", 15.0))
            add(DonateInfo("yomol.", 18.0))
            add(DonateInfo("miComet35P", 10.0))
            add(DonateInfo("347aidan", 5.0))
            add(DonateInfo("柳丝", 10.0))
            add(DonateInfo("221115", 5.0))
            add(DonateInfo("余小鱼?9?5", 21.1))
            add(DonateInfo("程", 20.0))
            add(DonateInfo("miro", 3.0))
            add(DonateInfo("你会振刀么", 18.88))
            add(DonateInfo("大挪移", 20.0))
            add(DonateInfo("小林", 10.0))
            add(DonateInfo("*林", 10.0))
            add(DonateInfo("221130", 6.0))
            add(DonateInfo("无", 5.0))
            add(DonateInfo("Mr.x", 20.0))
            add(DonateInfo("小妤", 13.14))
            add(DonateInfo("*方", 5.0))
            add(DonateInfo("渐渐简单", 5.0))
            add(DonateInfo("Steven", 20.0))
        }
    }
}