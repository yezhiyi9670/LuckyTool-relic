package com.luckyzyx.luckytool.utils

import android.content.Context
import android.widget.ImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.luckyzyx.luckytool.R

@Suppress("unused")
object DonateData {
    private const val CNU = "Null"
    private const val CQQ = "QQ"
    private const val CQQHB = "QQHB"
    private const val CWC = "WeChat"
    private const val CAP = "AliPay"
    private const val CPP = "PayPal"

    fun showQRCode(context: Context, base64: String) {
        val dialog = MaterialAlertDialogBuilder(
            context, dialogCentered
        ).apply {
            setTitle(context.getString(R.string.qq))
            setView(R.layout.layout_donate_dialog)
        }.show()
        dialog.findViewById<MaterialTextView>(R.id.donate_message)?.text =
            context.getString(R.string.donate_message)
        dialog.findViewById<ImageView>(R.id.donate_image)
            ?.setImageBitmap(base64ToBitmap(base64))
    }
}