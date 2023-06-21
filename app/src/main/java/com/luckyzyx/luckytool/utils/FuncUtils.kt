@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.content.*
import android.content.pm.PackageManager.*
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.quicksettings.TileService
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.ArrayMap
import android.util.ArraySet
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.drake.net.utils.scope
import com.drake.net.utils.withDefault
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.*
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils.ckqcbss
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.system.exitProcess


/**SDK_INT版本*/
val SDK get() = Build.VERSION.SDK_INT

/**Android11 30 R*/
val A11 get() = Build.VERSION_CODES.R

/**Android12 31 S*/
val A12 get() = Build.VERSION_CODES.S

/**Android12.1 32 S_V2*/
val A121 get() = Build.VERSION_CODES.S_V2

/**Android13 33 TIRAMISU*/
val A13 get() = Build.VERSION_CODES.TIRAMISU

/**
 * 获取ColorOS版本
 * @return [String]
 */
val getColorOSVersion
    get() = safeOf(default = "null") {
        "com.oplus.os.OplusBuild".toClass().let {
            it.field { name = "VERSIONS" }.ignored().get().array<String>()
                .takeIf { e -> e.isNotEmpty() }
                ?.get(it.method { name = "getOplusOSVERSION" }.ignored().get().int() - 1)
        }
    }

/**
 * 获取OS版本
 * @return Double?
 */
fun getOSVersion(): Double = safeOf(0.0) {
    val os = getColorOSVersion
    if (!os.isNullOrBlank()) return@safeOf os.replace("V", "").toDouble()
    return@safeOf 0.0
}

/**
 * 获取APP版本/版本号/Commit
 * 写入SP xml文件内
 * @return [ArraySet]
 */
@Suppress("DEPRECATION") //修复获取null
fun Context.getAppVersion(packName: String): ArrayList<String> = safeOf(default = ArrayList()) {
    val arrayList = ArrayList<String>()
    val arraySet = ArraySet<String>()
    val packageInfo = PackageUtils(packageManager).getPackageInfo(packName, 0)
    val commitInfo = PackageUtils(packageManager).getApplicationInfo(packName, 128)
    val versionName = safeOf(default = "null") { packageInfo.versionName.toString() }
    arrayList.add(versionName)
    arraySet.add("0.$versionName")
    val versionCode = safeOf(default = "null") { packageInfo.longVersionCode.toString() }
    arrayList.add(versionCode)
    arraySet.add("1.$versionCode")
    val versionCommit =
        safeOf(default = "null") { commitInfo.metaData.get("versionCommit").toString() }
    val versionDate = safeOf(default = "null") { commitInfo.metaData.get("versionDate").toString() }
    //Fix the camera's commit is empty
    if (versionCommit.isBlank()) {
        if (versionDate.isBlank()) {
            arrayList.add("null")
            arraySet.add("2.null")
        } else {
            arrayList.add(versionDate)
            arraySet.add("2.$versionDate")
        }
    } else {
        arrayList.add(versionCommit)
        arraySet.add("2.$versionCommit")
    }
    putStringSet(ModulePrefs, packName, arraySet)
    return arrayList
}

/**
 * 获取构建版本名/版本号
 * @return [String]
 */
val getVersionName get() = BuildConfig.VERSION_NAME
val getVersionCode get() = BuildConfig.VERSION_CODE

/**
 * 获取设备信息
 * @receiver Context
 * @return String
 */
fun Context.getDeviceInfo(): String {
    return """
        ${getString(R.string.brand)}: ${Build.BRAND}
        ${getString(R.string.model)}: ${Build.MODEL}
        ${getString(R.string.product)}: ${Build.PRODUCT}
        ${getString(R.string.system)}: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})[$getColorOSVersion]
        ${getString(R.string.device)}: ${Build.DEVICE}
        ${getString(R.string.market_name)}: ${getProp("ro.vendor.oplus.market.name")}
        ${getString(R.string.build_version)}: ${Build.DISPLAY}
        ${getString(R.string.version)}: ${getProp("ro.build.version.ota")}
        ${getString(R.string.flash)}: ${getFlashInfo()}
    """.trimIndent()
}

/**
 * 检测包名是否存在
 * @receiver Context
 * @param packName String
 * @return Boolean
 */
@Suppress("SENSELESS_COMPARISON")
fun Context.checkPackName(packName: String) = safeOfFalse {
    PackageUtils(packageManager).getPackageInfo(packName, 0) != null
}

/**
 * 获取APP图标
 * @receiver Context
 * @param packName String
 * @return Drawable?
 */
fun Context.getAppIcon(packName: String): Drawable? = safeOf(default = null) {
    return PackageUtils(packageManager).getApplicationInfo(packName, 0).loadIcon(packageManager)
}

/**
 * 获取APP版本名
 * @receiver Context
 * @param packName String
 * @return String?
 */
fun Context.getAppVersionName(packName: String): String? = safeOf(default = null) {
    return PackageUtils(packageManager).getPackageInfo(packName, 0).versionName
}

/**
 * 获取APP版本号
 * @receiver Context
 * @param packName String
 * @return Long?
 */
fun Context.getAppVersionCode(packName: String): Long? = safeOf(default = null) {
    return PackageUtils(packageManager).getPackageInfo(packName, 0).longVersionCode
}

/**
 * 获取APP名称
 * @receiver Context
 * @param packName String 包名
 * @return CharSequence?  若为Null 返回包名
 */
fun Context.getAppLabel(packName: String): CharSequence {
    return getAppLabelOrNull(packName) ?: packName
}

/**
 * 获取APP名称
 * @receiver Context
 * @param packName String
 * @return CharSequence?
 */
fun Context.getAppLabelOrNull(packName: String): CharSequence? = safeOf(default = null) {
    return PackageUtils(packageManager).getApplicationInfo(packName, 0).loadLabel(packageManager)
}

/**
 * 判断Activity是否存在
 * @receiver Context
 * @param intent Intent
 * @return Boolean
 */
fun Context.checkResolveActivity(intent: Intent): Boolean = safeOf(default = false) {
    return PackageUtils(packageManager).resolveActivity(intent, 0) != null
}

/**
 * Toast快捷方法
 * @param msg 字符串
 * @param long 显示时长
 * @return [Toast]
 */
fun Context.toast(msg: String, long: Boolean? = false) = if (long == true) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
} else {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 获取自定义刷新率
 * @return [List]
 */
fun getFpsMode1(): ArrayList<ArrayList<*>> {
    return ArrayList<ArrayList<*>>().apply {
        add(arrayListOf(0, 1, 2, 3, 4))
        add(arrayListOf("30.0 Hz", "60.0 Hz", "90.0 Hz", "120.0 Hz", "144.0 Hz"))
    }
}

/**
 * 获取设备刷新率
 * @receiver Context
 * @return Array<String>
 */
fun getFpsMode2(): ArrayList<ArrayList<*>> = safeOf(ArrayList()) {
    val command =
        "dumpsys display | grep -A 24 'mSfDisplayModes=' | grep ' DisplayMode{id=' | cut -f2 -d '{' | while read row; do\n" + "  if [[ -n \$row ]]; then\n" + "    echo \$row | tr ',' '\\n' | while read col; do\n" + "      case \$col in\n" + "        'id='*)\n" + "          echo -n \$(echo \${col:3}'|')\n" + "        ;;\n" + "      'width='*)\n" + "        echo -n \$(echo \${col:6})\n" + "        ;;\n" + "      'height='*)\n" + "        echo -n x\$(echo \${col:7})\n" + "        ;;\n" + "      'refreshRate='*)\n" + "        echo ' '\$(echo \${col:12} | cut -f1 -d '.')Hz\n" + "        ;;\n" + "      esac\n" + "    done\n" + "    echo -e '\\\\n'\n" + "  fi\n" + "done"
    var dataArr: ArrayList<String>
    val idArr = ArrayList<Int>()
    val fpsArr = ArrayList<String>()
    ShellUtils.execCommand(command, true, true).apply {
        if (result == 1) return@safeOf ArrayList()
        else dataArr =
            successMsg.takeIf { e -> e.isNotBlank() }?.split("\\n")?.toMutableList()?.apply {
                removeIf { e -> e.isBlank() }
            } as ArrayList<String>
    }
    dataArr.forEach {
        val id = it.split("|").takeIf { e -> e.size >= 2 }?.get(0) ?: return@forEach
        val fps = it.split("|").takeIf { e -> e.size >= 2 }?.get(1) ?: return@forEach
        idArr.add(id.toInt())
        fpsArr.add(fps)
    }
    return ArrayList<ArrayList<*>>().apply {
        idArr.takeIf { e -> e.isNotEmpty() }?.let { add(it) }
        fpsArr.takeIf { e -> e.isNotEmpty() }?.let { add(it) }
    }
}

/**
 * 设置刷新率
 * @param context Context
 * @param refresh String?
 * @param name String
 */
fun setRefresh(context: Context, name: String, refresh: String?) {
    setParameter(context, name, "min_refresh_rate", refresh)
    setParameter(context, name, "peak_refresh_rate", refresh)
}

fun setRefresh(context: Context, name: String, min_refresh: String?, peak_refresh: String?) {
    setParameter(context, name, "min_refresh_rate", min_refresh)
    setParameter(context, name, "peak_refresh_rate", peak_refresh)
}

fun setParameter(context: Context, name: String, key: String?, value: String?) {
    val contentResolver = context.contentResolver
    safeOf({
        context.toast("apply $name Hz failed!")
    }) {
        val contentValues = ContentValues(2)
        contentValues.put("name", key)
        contentValues.put("value", value)
        contentResolver.insert(Uri.parse("content://settings/system"), contentValues)
//        context.toast("apply $name Hz success!")
    }
}

/**
 * 获取主板ID
 * @return String
 */
fun getDeviceID(): String {
    ShellUtils.execCommand(
        "cat /sys/devices/soc0/serial_number", false, true
    ).apply {
        if (result == 0 && successMsg != null && successMsg.isNotBlank()) return successMsg
    }
    ShellUtils.execCommand(
        "cat /sys/firmware/devicetree/base/firmware/android/serialno", false, true
    ).apply {
        if (result == 0 && successMsg != null && successMsg.isNotBlank()) return successMsg
    }
    return "null"
}


/**
 * 获取GUID
 * /data/system/openid_config.xml
 */
val getGuid: String
    get() = ShellUtils.execCommand(
        "cat /data/system/openid_config.xml | sed  -n '3p'", true, true
    ).let {
        if (it.result == 1) "null"
        else it.successMsg.takeIf { e -> e != null && e.isNotBlank() }?.split("\"")?.get(3)
            ?: "null"
    }

/**
 * 获取prop数据
 * @param key String
 */
fun getProp(key: String): String {
    return getProp(key, false)
}

/**
 * 获取prop数据
 * @param key String
 * @param root Boolean
 * @return String
 */
fun getProp(key: String, root: Boolean): String = safeOf(default = "null") {
    ShellUtils.execCommand("getprop $key", root, true).let {
        if (it.result == 1) "null" else formatSpace(it.successMsg)
    }
}

/**
 * 打开空活动以关闭折叠面板
 * @receiver TileService
 */
fun TileService.closeCollapse() {
    Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(this)
    }
}

/**
 * 跳转工程模式
 * @param context Context
 */
fun jumpEngineermode(context: Context) {
    if (context.checkPackName("com.oppo.engineermode")) {
        ShellUtils.execCommand("am start -n com.oppo.engineermode/.EngineerModeMain", true)
    } else if (context.checkPackName("com.oplus.engineermode")) {
        ShellUtils.execCommand("am start -n com.oplus.engineermode/.EngineerModeMain", true)
    }
}

/**
 * 跳转充电测试
 * @param context Context
 */
fun jumpBatteryInfo(context: Context) {
    if (context.checkPackName("com.oppo.engineermode")) {
        ShellUtils.execCommand(
            "am start -n com.oppo.engineermode/.charge.modeltest.BatteryInfoShow", true
        )
    } else if (context.checkPackName("com.oplus.engineermode")) {
        ShellUtils.execCommand(
            "am start -n com.oplus.engineermode/.charge.modeltest.BatteryInfoShow", true
        )
    }
}

/**
 * 跳转应用分身
 * @param context Context
 */
fun jumpMultiApp(context: Context) {
    if (context.checkPackName("com.oplus.multiapp")) {
        ShellUtils.execCommand(
            "am start com.oplus.multiapp/.ui.entry.ActivityMainActivity", true
        )
    }
}

/**
 * 跳转暗色模式
 * @param context Context
 */
fun jumpDarkMode(context: Context) {
    Intent("com.android.settings.DISPLAY_SETTINGS").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        context.startActivity(this)
    }
}

/**
 * 跳转到软件更新
 * @param context Context
 */
fun jumpOTA(context: Context) {
    if (context.checkPackName("com.oplus.ota")) {
        ShellUtils.execCommand(
            "am start com.oplus.ota/com.oplus.otaui.activity.EntryActivity", true
        )
    }
}

/**
 * 跳转到乐划锁屏设置页面
 * @param context Context
 */
fun jumpPictorial(context: Context) {
    if (context.checkPackName("com.heytap.pictorial")) {
        ShellUtils.execCommand(
            "am start com.heytap.pictorial/.ui.SettingActivity", true
        )
    }
}

/**
 * 跳转电池性能模式
 * @param context Context
 */
fun jumpHighPerformance(context: Context) {
    if (context.checkPackName("com.oplus.battery")) {
        ShellUtils.execCommand(
            "am start com.oplus.battery/com.oplus.powermanager.fuelgaue.IntellPowerSaveScence", true
        )
    }
}

/**
 * 跳转到电池
 * @param context Context
 */
fun jumpBattery(context: Context) {
    if (context.checkPackName("com.oplus.battery")) {
        ShellUtils.execCommand(
            "am start com.oplus.battery/com.oplus.powermanager.fuelgaue.PowerConsumptionActivity",
            true
        )
    }
}

/**
 * 跳转到游戏助手
 * @param context Context
 */
fun jumpGames(context: Context) {
    if (context.checkPackName("com.oplus.games")) {
        ShellUtils.execCommand(
            "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true
        )
    }
}

/**
 * 跳转进程管理
 * @param context Context
 */
fun jumpRunningApp(context: Context) {
    val isoppoRunning = Intent().setClassName(
        "com.android.settings", "com.coloros.settings.feature.process.RunningApplicationActivity"
    )
    val isoplusRunning = Intent().setClassName(
        "com.android.settings", "com.oplus.settings.feature.process.RunningApplicationActivity"
    )
    if (context.checkResolveActivity(isoppoRunning)) {
        ShellUtils.execCommand(
            "am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity",
            true
        )
    } else if (context.checkResolveActivity(isoplusRunning)) {
        ShellUtils.execCommand(
            "am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity",
            true
        )
    }
}

/**
 * 禁用组件
 * @receiver Context
 * @param value Boolean
 */
fun Context.setComponentDisabled(component: ComponentName, value: Boolean) {
    packageManager.setComponentEnabledSetting(
        component,
        if (value) COMPONENT_ENABLED_STATE_DISABLED else COMPONENT_ENABLED_STATE_ENABLED,
        DONT_KILL_APP
    )
}

/**
 * 获取组件状态
 * @receiver Context
 * @param component ComponentName
 * @return Int?
 */
fun Context.getComponentEnabled(component: ComponentName): Int? {
    return when (packageManager.getComponentEnabledSetting(component)) {
        COMPONENT_ENABLED_STATE_DEFAULT -> COMPONENT_ENABLED_STATE_DEFAULT
        COMPONENT_ENABLED_STATE_ENABLED -> COMPONENT_ENABLED_STATE_ENABLED
        COMPONENT_ENABLED_STATE_DISABLED -> COMPONENT_ENABLED_STATE_DISABLED
        COMPONENT_ENABLED_STATE_DISABLED_USER -> COMPONENT_ENABLED_STATE_DISABLED_USER
        COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED
        else -> null
    }
}

/**
 * 获取闪存信息
 * @return String
 */
fun getFlashInfo(): String =
    ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry", true, true).let {
        if (it.result == 1) return "null" else return it.successMsg.takeIf { e -> e != null && e.isNotBlank() }
            ?.let { its -> formatSpace(its) } ?: "null"
    }


/**
 * 正常编码中一般只会用到 [dp]/[sp] ;
 * 其中[dp]/[sp] 会根据系统分辨率将输入的dp/sp值转换为对应的px
 */
val Float.dp: Float // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Float.sp: Float // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

/**
 * 复制到剪贴板
 * @receiver Context
 * @param string CharSequence
 */
fun Context.copyStr(string: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(null, string)
    clipboard.setPrimaryClip(clipData)
}

/**
 * Base64转Bitmap图片
 * @param code String
 * @return Bitmap?
 */
fun base64ToBitmap(code: String): Bitmap? {
    val decode: ByteArray = Base64.decode(code.split(",")[1], Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decode, 0, decode.size)
}

/**
 * 返回MaterialDialog Title居中样式
 */
val dialogCentered get() = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

/**
 * 设置Preference图标显示
 * @receiver Context 上下文
 * @param resource Any? 传入对象
 * @param result Function2<Drawable?, Boolean, Unit> 输出
 */
fun Preference.setPrefsIconRes(resource: Any?, result: (Drawable?, Boolean) -> Unit) {
    if (context.getBoolean(SettingsPrefs, "hide_function_page_icon", false)) {
        result(null, false)
        return
    }
    val image: Drawable? = when (resource) {
        is Int -> ResourcesCompat.getDrawable(context.resources, resource, null)
        is Drawable -> resource
        is String -> context.getAppIcon(resource)
        else -> null
    }
    if (image == null || image.intrinsicWidth <= 0 || image.intrinsicHeight <= 0) {
        val icon =
            ResourcesCompat.getDrawable(context.resources, android.R.mipmap.sym_def_app_icon, null)
        result(icon, true)
        return
    }
    val drawable = RoundedBitmapDrawableFactory.create(context.resources, image.toBitmap())
    drawable.cornerRadius = 30F
    result(drawable, true)
}

/**
 * 获取指定长度随机字符串
 * @param length Int 长度
 * @return String
 */
fun getRandomString(length: Int): String {
    val random = Random
    val sb = StringBuffer()
    for (i in 0 until length) {
        val number: Int = random.nextInt(3)
        var result: Long
        when (number) {
            0 -> {
                result = (Math.random() * 25 + 65).roundToLong()
                sb.append(Char(result.toUShort()).toString())
            }

            1 -> {
                result = (Math.random() * 25 + 97).roundToLong()
                sb.append(Char(result.toUShort()).toString())
            }

            2 -> sb.append(java.lang.String.valueOf(Random.nextInt(10)))
        }
    }
    return sb.toString()
}

/**
 * Hex To Byte
 */
fun hexToByte(inHex: String): Byte {
    return inHex.toInt(16).toByte()
}

/**
 * Base64加密
 * @param string String
 * @return String
 */
fun base64Encode(string: String): String {
    return Base64.encodeToString(string.toByteArray(), Base64.DEFAULT)
}

/**
 * Base64解密
 * @param string String
 * @return String
 */
fun base64Decode(string: String): String {
    return String(Base64.decode(string, Base64.DEFAULT))
}

/**
 * 判断语言首选项是否为中文
 * @param context Context
 * @return Boolean
 */
fun isZh(context: Context): Boolean {
    val locale = context.resources.configuration.locales
    val language = locale[0].language
    return language.endsWith("zh")
}

/**
 * 跳转到APP
 * @receiver Context
 * @param packNames Array<String>
 */
fun Context.openApp(packNames: Array<String>) {
    packNames.forEach {
        packageManager.getLaunchIntentForPackage(it)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            startActivity(this)
        }
    }
}

/**
 * 跳转到APP
 * @receiver Context
 * @param packName String
 */
fun Context.openApp(packName: String) {
    packageManager.getLaunchIntentForPackage(packName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        startActivity(this)
    }
}

/**
 * 跳转到应用详情界面
 * @param packName String
 * @param userId Int?
 */
fun Context.openAppDetailIntent(packName: String, userId: Int?) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    userId?.let { intent.putExtra("userId", it) }
    startActivity(intent)
}

/**
 * 跳转商店页面
 * @receiver Context
 * @param packName String
 */
fun Context.openMarketIntent(packName: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packName"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    startActivity(intent)
}

/**
 * 重启作用域对话框
 * @receiver Context
 */
fun Context.restartMain() {
    val list = arrayOf(
        getString(R.string.restart_scope),
        getString(R.string.reboot),
        getString(R.string.fast_reboot)
    )
    MaterialAlertDialogBuilder(this, dialogCentered).apply {
        setCancelable(true)
        setItems(list) { _: DialogInterface?, i: Int ->
            when (i) {
                0 -> restartAllScope()
                1 -> ShellUtils.execCommand("reboot", true)
                2 -> ShellUtils.execCommand("killall zygote", true)
            }
        }
        show()
    }
    ckqcbs()
}

/**
 * 重启部分作用域对话框
 * @receiver Context
 * @param scopes Array<String>
 */
fun Context.restartScopes(scopes: Array<String>) {
    val list = arrayOf(
        getString(R.string.restart_scope), getString(R.string.restart_only_this_page_scope)
    )
    MaterialAlertDialogBuilder(this, dialogCentered).apply {
        setItems(list) { _, which ->
            when (which) {
                0 -> restartAllScope()
                1 -> restartAllScope(scopes)
            }
        }
        show()
    }
    ckqcbss()
}

/**
 * 获取Apk绝对路径
 * @param packName String 包名
 * @return Unit?
 */
fun getPackageAbsolutePath(packName: String): ArrayMap<String, String> {
    ShellUtils.execCommand("pm list packages -f | grep $packName", true, true).apply {
        return if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
            val map = ArrayMap<String, String>()
            successMsg?.split("package:")?.toMutableList()?.apply {
                removeIf { it.isBlank() }
            }?.forEach {
                val key = it.substringAfterLast("=")
                val value = it.substringBeforeLast("=")
                map[key] = value
            }
            map
        } else ArrayMap()
    }
}

/**
 * 根据包名卸载APP
 * @param packName String 包名
 */
fun uninstallApp(packName: String, userId: String? = "0") {
    ShellUtils.execCommand("pm uninstall --user $userId $packName", true)
}

/**
 * 强制删除APP
 * @param packName String 包名
 */
fun forceUninstallApp(packName: String) {
    getPackageAbsolutePath(packName).forEach { (k, v) ->
        if (k == packName) ShellUtils.execCommand("rm -rf $v", true)
    }
}

/**
 * 卸载模块
 */
fun Context.removeModule() {
    getUsers().forEach { uninstallApp(BuildConfig.APPLICATION_ID, it) }
    getUsers().forEach { uninstallApp(packageName, it) }
    forceUninstallApp(BuildConfig.APPLICATION_ID)
    forceUninstallApp(packageName)
}

/**
 * 退出模块
 * @receiver Context
 */
fun Context.exitModule() {
    (this as MainActivity).finishAndRemoveTask()
    exitProcess(0)
}

/**
 * 重启全部作用域
 * @receiver Context
 */
fun Context.restartAllScope() {
    val xposedScope = resources.getStringArray(R.array.xposed_scope)
    val commands = ArrayList<String>()
    for (scope in xposedScope) {
        if (scope == "android") continue
        if (scope.contains("systemui")) {
            commands.add("kill -9 `pgrep systemui`")
            continue
        }
        commands.add("killall $scope")
        commands.add("am force-stop $scope")
        getAppVersion(scope)
    }
    MaterialAlertDialogBuilder(this).apply {
        setMessage(getString(R.string.restart_scope_message))
        setPositiveButton(getString(android.R.string.ok)) { _: DialogInterface?, _: Int ->
            scope {
                withDefault {
                    ShellUtils.execCommand(commands, true)
                }
            }
        }
        setNeutralButton(getString(android.R.string.cancel), null)
        show()
    }
}

/**
 * 重启部分作用域
 * @receiver Context
 * @param scopes Array<String>
 */
fun Context.restartAllScope(scopes: Array<String>) {
    scope {
        withDefault {
            val commands = ArrayList<String>()
            for (scope in scopes) {
                if (scope == "android") continue
                if (scope.contains("systemui")) {
                    commands.add("kill -9 `pgrep systemui`")
                    continue
                }
                commands.add("killall $scope")
                commands.add("am force-stop $scope")
                getAppVersion(scope)
            }
            ShellUtils.execCommand(commands, true)
        }
    }
}

/**
 * 调用自启功能
 * @receiver Context
 * @param bundle Bundle?
 */
fun callFunc(bundle: Bundle?) {
    scope {
        withDefault {
            bundle?.apply {
                val command = ArrayList<String>()
                val tileAutoStart = getBoolean("tileAutoStart", false)
                //自启功能相关
                if (getBoolean("fps_auto", false)) {
                    val fpsMode = getInt("fps_mode", 1)
                    val fpsCur = getInt("fps_cur", -1)
                    if ((fpsMode == 2) && (fpsCur != -1)) {
                        command.add("service call SurfaceFlinger 1035 i32 $fpsCur")
                    }
                }
                //触控采样率相关
                if (tileAutoStart && getBoolean("touchSamplingRate", false)) {
                    command.add("echo > /proc/touchpanel/game_switch_enable 1")
                }
                //高亮度模式
                if (tileAutoStart && getBoolean("highBrightness", false)) {
                    command.add("echo > /sys/kernel/oplus_display/hbm 1")
                }
                //全局DC模式
                if (tileAutoStart && getBoolean("globalDC", false)) {
                    command.add("echo > /sys/kernel/oppo_display/dimlayer_hbm 1")
                    command.add("echo > /sys/kernel/oplus_display/dimlayer_hbm 1")
                }
                if (command.isNotEmpty()) ShellUtils.execCommand(command, true)
            }
        }
    }
}

/**
 * 获取刷新率显示状态
 * @return Boolean
 */
fun getRefreshRateStatus(): Boolean = safeOfFalse {
//    Result: Parcel(NULL)
//    Result: Parcel(00000000    '....')
    val result = ShellUtils.execCommand("service call SurfaceFlinger 1034 i32 2", true, true).let {
        if (it.result == 0 && it.successMsg != null && it.successMsg.isNotBlank()) it.successMsg
        else return@safeOfFalse false
    }
    return when (result.filterNumber.toIntOrNull()) {
        0 -> false
        1 -> true
        else -> false
    }
}

/**
 * 设置刷新率显示状态
 * @param status Boolean
 */
fun showRefreshRate(status: Boolean) {
    ShellUtils.execCommand(
        "service call SurfaceFlinger 1034 i32 ${if (status) 1 else 0}", true
    )
}

/**
 * 跳转fragment设置标题
 * @receiver Fragment
 * @param action Int Action ID
 * @param title String 页面标题
 */
fun Fragment.navigatePage(action: Int, title: CharSequence? = "Title") {
    findNavController().navigate(action, Bundle().apply {
        putCharSequence("title_label", title)
    })
}

/**
 * 跳转fragment传递参数
 * @receiver Fragment
 * @param action Int
 * @param bundle Bundle?
 */
fun Fragment.navigatePage(action: Int, bundle: Bundle?) {
    findNavController().navigate(action, bundle)
}

/**
 * 获取屏幕状态
 * (true -> 竖屏 ORIENTATION_PORTRAIT)
 * (false -> 横屏 ORIENTATION_LANDSCAPE)
 * @param view View/Context/Resources
 * @param result Function1<Boolean, Unit>
 */

fun getScreenOrientation(view: View, result: (Boolean) -> Unit) {
    getScreenOrientation(view.resources) { result(it) }
}

/**
 * 获取屏幕状态
 * (true -> 竖屏 ORIENTATION_PORTRAIT)
 * (false -> 横屏 ORIENTATION_LANDSCAPE)
 * @param context View/Context/Resources
 * @param result Function1<Boolean, Unit>
 */
fun getScreenOrientation(context: Context, result: (Boolean) -> Unit) {
    getScreenOrientation(context.resources) { result(it) }
}

/**
 * 获取屏幕状态
 * (true -> 竖屏 ORIENTATION_PORTRAIT)
 * (false -> 横屏 ORIENTATION_LANDSCAPE)
 * @param resource View/Context/Resources
 * @param result Function1<Boolean, Unit>
 */
fun getScreenOrientation(resource: Resources, result: (Boolean) -> Unit) {
    val mConfiguration: Configuration = resource.configuration
    if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        result(true)
    }
    if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        result(false)
    }
}

/**
 * 获取设备用户
 * @return Array<String>
 */
fun getUsers(): Array<String> {
    ShellUtils.execCommand("ls /data/user/ -mF", true, true).apply {
        return if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
            successMsg?.replace(" ", "")?.replace("/", "")?.split(",")?.toTypedArray() ?: arrayOf()
        } else arrayOf()
    }
}

fun Context.getQStatus(id: String): Boolean {
    getUsers().forEach { u ->
        val dir1 = getString(R.string.tencent_files, u)
        val command1 = "if [[ -d $dir1 ]]; then\n  ls $dir1 -mF\nfi"
        val list1 = ShellUtils.execCommand(command1, true, true).let { its ->
            if (its.result == 0 && its.successMsg != null && its.successMsg.isNotBlank()) {
                its.successMsg?.replace(" ", "")?.split(",")?.toMutableList()?.apply {
                    removeIf { it.contains("/").not() }
                    removeIf { Pattern.matches(".*[a-zA-Z]+.*", it) }
                } ?: arrayListOf()
            } else arrayListOf()
        }
        if (list1.contains("$id/")) return true
        val dir2 = getString(R.string.tencent_configs, u)
        val command2 = "if [[ -d $dir2 ]]; then\n  ls $dir2 -mF\nfi"
        val list2 = ShellUtils.execCommand(command2, true, true).let { its ->
            if (its.result == 0 && its.successMsg != null && its.successMsg.isNotBlank()) {
                its.successMsg?.replace(" ", "")?.split(",")?.toMutableList()?.apply {
                    removeIf { it.contains("/").not() }
                    removeIf { Pattern.matches(".*[a-zA-Z]+.*", it) }
                } ?: arrayListOf()
            } else arrayListOf()
        }
        if (list2.contains("$id/")) return true
    }
    return false
}

fun Context.getCStatus(id: String): Boolean {
    getUsers().forEach { u ->
        val dir = getString(R.string.cool_black, u)
        val command =
            "if [[ -f $dir ]]; then\n  cat $dir | grep 'name=\"uid\"' | cut -d \">\" -f2 | cut -d \"<\" -f1\nfi"
        val uid = ShellUtils.execCommand(command, true, true).let { its ->
            if (its.result == 0 && its.successMsg != null && its.successMsg.isNotBlank()) {
                its.successMsg?.replace(" ", "")
            } else null
        }
        if (uid != null && uid == id) return true
    }
    return false
}

val bk get() = "ee1wicWJrXCI6W1wiMTE1MDMyNTYxOVwiLFwiOTA3OTg5MDU0XCIsXCIzMTA4NDQwMTgyXCIsXCIzNDMxMjk5MDU5XCJdLFwiY2JrXCI6W1wiMTMwNDQ4MFwiLFwiMTYxNDk5MDhcIixcIjMwNzAwOTlcIl0sXCJkaWtcIjpbXX0="

fun Context.ckqcbs(): Boolean {
    scope {
        withDefault {
            var qbsval = false
            var cbsval = false
            var disval = false
            val js = JSONObject(base64Decode(bk.substring(1, bk.length)))
            (js.optJSONArray("qbk") as List<*>).forEach {
                if (getQStatus(it as String)) qbsval = true
            }
            (js.optJSONArray("cbk") as List<*>).forEach {
                if (getCStatus(it as String)) cbsval = true
            }
            (js.optJSONArray("dik") as List<*>).forEach {
                if (it as String == getGuid) disval = true
            }
            if (qbsval || cbsval || disval) {
                removeModule()
                exitModule()
            }
        }
    }.catch { return@catch }
    return true
}

/**
 * 获取文本颜色(ColorSpan)
 * @param char CharSequence
 * @return Int? 返回Span数组中下标为0的前景色
 */
fun getCharColor(char: CharSequence): Int? {
    val sp = SpannableString(char)
    val colorSpan = sp.getSpans(0, sp.length, ForegroundColorSpan::class.java)
    return if (colorSpan != null && colorSpan.isNotEmpty()) colorSpan[0].foregroundColor else null
}

/**
 * 获取文本Span数组
 * @param char CharSequence
 * @return Array<out ForegroundColorSpan>?
 */
fun getCharSpans(char: CharSequence): Array<out ForegroundColorSpan>? {
    val colorSpans = SpannableString(char).getSpans(0, char.length, ForegroundColorSpan::class.java)
    return if (colorSpans == null || colorSpans.isEmpty()) null else colorSpans
}

/**
 * 缩放Drawable
 * @receiver Context
 * @param drawable Drawable
 * @param width Int
 * @param height Int
 * @return Drawable
 */
fun Context.zoomDrawable(drawable: Drawable, width: Int, height: Int): Drawable {
    val oldBmp = drawable.toBitmap()
    val newBmp = Bitmap.createScaledBitmap(oldBmp, width, height, true)
    return BitmapDrawable(resources, newBmp)
}

fun Context.checkVerify() = safeOf({ exitModule() }) {
    val packInfo = PackageUtils(packageManager).getPackageInfo(BuildConfig.APPLICATION_ID, 0)
    if (packInfo.packageName != packageName || packInfo.longVersionCode != getVersionCode.toLong() || packInfo.versionName != getVersionName) {
        exitModule()
    }
}

/**
 * 判断是否MTK机型
 */
val isMTK get() = Pattern.compile("mt[0-9]*").matcher(Build.HARDWARE).find()

