package com.luckyzyx.luckytool.hook.scope.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType
import com.highcapable.yukihookapi.hook.type.java.StringType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Source OnePlusWidget
        //Search CharSequence Field
        //Source update one plus clock +2 / +4 -> setTextViewText
        val clazz = "com.coloros.widget.smallweather.OnePlusWidget".toClass()
        val isSearch = clazz.hasMethod {
            param(StringType, StringType)
            paramCount = 2
            returnType = CharSequenceType
        }
        if (isSearch) {
            clazz.field {
                type(CharSequenceType).index().first()
            }.get().set("")
            return
        }
        searchClass {
            from("m0").absolute()
            field {
                type = CharSequenceType
            }.count(1)
            field {
                type = HandlerClass
            }.count(1)
            field {
                type = BooleanType
            }.count(3)
            method {
                param(ContextClass, StringType, StringType)
                returnType = CharSequenceType
            }.count(1)
            method {
                param(ContextClass, StringType)
                returnType = CharSequenceType
            }.count(1)
        }.get()?.field {
            type(CharSequenceType).index().first()
        }?.get()?.set("") ?: loggerD(msg = "$packageName\nError -> RemoveAlarmClockWidgetRedOne")
    }
}






