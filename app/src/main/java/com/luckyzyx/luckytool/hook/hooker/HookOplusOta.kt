package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object HookOplusOta : YukiBaseHooker() {
    override fun onHook() {

        //local_update_failed_not_match 安装包不匹配
        //local_update_failed_read_exception 读取文件错误
        //local_update_failed_not_exist 文件不存在
        //local_update_low_memory 存储空间不足不能安装

        //local_update_verify_failed 验证失败
        //unzip_file_failed 解压失败

    }
}