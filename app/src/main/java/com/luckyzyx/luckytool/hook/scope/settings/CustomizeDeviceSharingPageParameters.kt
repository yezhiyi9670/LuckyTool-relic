package com.luckyzyx.luckytool.hook.scope.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEach
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.utils.appcompat.dialog.COUIAlertDialogBuilder

object CustomizeDeviceSharingPageParameters : YukiBaseHooker() {

    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source ShareAboutPhoneActivity
        findClass("com.oplus.settings.feature.deviceinfo.aboutphone.ShareAboutPhoneActivity").hook {
            injectMember {
                method { name = "onCreate" }
                afterHook {
                    val activity = instance<Activity>()
                    val shareViewId = activity.resources.getIdentifier(
                        "share_view", "id", packageName
                    ).takeIf { it != 0 } ?: return@afterHook
                    val shareView = activity.findViewById<LinearLayout>(shareViewId)
                        ?: return@afterHook

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

    private fun TextView.setClickInfo() {
        setOnClickListener {
            COUIAlertDialogBuilder(context, "COUIAlertDialog.SingleInput", appClassLoader).apply {
                var editText: EditText? = null
                var dialog: Any? = null
                dialog = builder?.apply {
                    setTitle(text)
                    setNegativeButton(android.R.string.cancel, null)
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        val newText = editText?.text as CharSequence
                        if (newText.isNotBlank()) text = newText
                        dialog?.dismiss()
                    }
                }?.show()
                editText = dialog?.getEditText("edit_text_1")
                editText?.setText(text)
            }
        }
    }
}