package com.luckyzyx.luckytool.hook.scope.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEach
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType

object CustomizeDeviceSharingPageParameters : YukiBaseHooker() {

    var dialog: Any? = null
    var editText: Any? = null

    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source ShareAboutPhoneActivity
        findClass("com.oplus.settings.feature.deviceinfo.aboutphone.ShareAboutPhoneActivity").hook {
            injectMember {
                method { name = "onCreate" }
                afterHook {
                    val activity = instance<Activity>()
                    val shareViewId =
                        activity.resources.getIdentifier("share_view", "id", packageName)
                    if (shareViewId == 0) return@afterHook
                    val shareView =
                        activity.findViewById<LinearLayout>(shareViewId) ?: return@afterHook

                    shareView.children.forEach {
                        //title_phone_ly / bran_share_card / about_share_card_bg
                        if (it is ViewGroup) it.forEach { it2 ->
                            if (it2 is TextView) it2.setClickInfo()
                            else if (it2 is ViewGroup) it2.forEach { it3 ->
                                if (it3 is TextView) it3.setClickInfo()
                                else if (it3 is ViewGroup) it3.forEach { it4 ->
                                    if (it4 is TextView) it4.setClickInfo()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun TextView.setClickInfo() {
        dialog = null
        editText = null
        setOnClickListener {
            val dialogCls = "com.coui.appcompat.dialog.COUIAlertDialogBuilder".toClass()
            val cOUIAlertDialogSingleInput = context.resources.getIdentifier(
                "COUIAlertDialog.SingleInput", "style", packageName
            )
            val dialogBuilder =
                dialogCls.buildOf(context, cOUIAlertDialogSingleInput) {
                    param(ContextClass, IntType)
                }
            dialogBuilder?.current()?.method { name = "setTitle";param(CharSequenceClass) }
                ?.call(text)
            dialogBuilder?.current()
                ?.method { name = "setNegativeButton";param { it[0] == IntType } }
                ?.call(android.R.string.cancel, null)
            dialogBuilder?.current()
                ?.method { name = "setPositiveButton";param { it[0] == IntType } }
                ?.call(android.R.string.ok, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val newText = editText?.current()?.method {
                            name = "getText";superClass()
                        }?.call()
                        if ((newText as CharSequence).isNotBlank()) text = newText
                        dialog?.current()?.method { name = "dismiss";superClass() }?.call()
                    }
                })
            dialog = dialogBuilder?.current()?.method { name = "show" }?.call()

            val editId = context.resources.getIdentifier("edit_text_1", "id", packageName)
            val inputView = dialog?.current()?.method {
                name = "findViewById";param(IntType);superClass()
            }?.call(editId)
            editText = inputView?.current()?.method { name = "getEditText" }?.call()
            editText?.current()?.method {
                name = "setText";param(CharSequenceClass);superClass()
            }?.call(text)
        }
    }
}