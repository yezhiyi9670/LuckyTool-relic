@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.luckyzyx.luckytool.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.luckyzyx.luckytool.R

object NotifyUtils {
    /**通知权限*/
    private const val POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"

    /**默认通知渠道*/
    const val DEFAULT_NOTICE_ID = "default"
    const val DEFAULT_NOTICE_NAME = "默认通知"
    const val DEFAULT_NOTICE_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT

    /**
     * 获取DEMO通知
     * @param context Context
     * @return Notification
     */
    fun getDemoNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, DEFAULT_NOTICE_ID)
            .setSmallIcon(R.drawable.ic_baseline_info_24)
            .setContentTitle("标题")
            .setContentText("内容")
//            .setStyle(NotificationCompat.BigTextStyle().bigText(""))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    /**
     * 检查是否授予权限
     * @param context Context
     * @return Boolean
     */
    fun checkPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 发送通知
     * @param context Context
     * @param notifyId Int 通知ID
     * @param notification Notification 通知
     */
    fun sendNotification(context: Context, notifyId: Int, notification: Notification) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifyId, notification)
    }

    /**
     * 移除通知
     * @param context Context
     * @param notifyId Int 通知ID
     */
    fun clearNotification(context: Context, notifyId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notifyId)
    }

    /**
     * 创建通知渠道
     * @param context Context
     * @param channel NotificationChannel Channel
     */
    fun createChannel(context: Context, channel: NotificationChannel) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 删除通知渠道
     * @param context Context
     * @param channelId String 渠道ID
     */
    fun deleteChannel(context: Context, channelId: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(channelId)
    }

    /**
     * 请求权限
     * @param activity Activity
     */
    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkPermission(activity)) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        POST_NOTIFICATIONS
                    )
                ) {
                    enableNotification(activity)
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(POST_NOTIFICATIONS), 100)
                }
            }
        } else {
            val enabled = NotificationManagerCompat.from(activity).areNotificationsEnabled()
            if (!enabled) {
                enableNotification(activity)
            }
        }
    }

    /**
     * 跳转应用通知
     * @param context Context
     */
    private fun enableNotification(context: Context) {
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }
    }
}