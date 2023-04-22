@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.data

import android.content.*
import android.content.pm.PackageManager.*
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.ArraySet
import android.util.Base64
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.drake.net.utils.scope
import com.drake.net.utils.withIO
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.tools.*
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToLong
import kotlin.random.Random

/**SDK_INT版本*/
val SDK get() = Build.VERSION.SDK_INT

/**A11 R*/
val A11 get() = Build.VERSION_CODES.R

/**A12 S*/
val A12 get() = Build.VERSION_CODES.S

/**A12.1 S_V2*/
val A121 get() = Build.VERSION_CODES.S_V2

/**A13 TIRAMISU*/
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
 * @param packName String
 * @return CharSequence?
 */
fun Context.getAppLabel(packName: String): CharSequence? = safeOf(default = null) {
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
 * @param name 字符串
 * @param long 显示时长
 * @return [Toast]
 */
internal fun Context.toast(name: String, long: Boolean? = false): Any = if (long == true) {
    Toast.makeText(this, name, Toast.LENGTH_LONG).show()
} else {
    Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
}

/**
 * 获取自定义刷新率
 * @return [List]
 */
fun getFpsMode1(): ArrayList<ArrayList<*>> {
    return ArrayList<ArrayList<*>>().apply {
        add(arrayListOf(0, 1, 2, 3))
        add(arrayListOf("30.0 Hz", "60.0 Hz", "90.0 Hz", "120.0 Hz"))
    }
}

/**
 * 获取设备刷新率
 * @receiver Context
 * @return Array<String>
 */
fun getFpsMode2(): ArrayList<ArrayList<*>> = safeOf(ArrayList()) {
    val command =
        "dumpsys display | grep -A 24 'mSfDisplayModes=' | grep ' DisplayMode{id=' | cut -f2 -d '{' | while read row; do\n" +
                "  if [[ -n \$row ]]; then\n" +
                "    echo \$row | tr ',' '\\n' | while read col; do\n" +
                "      case \$col in\n" +
                "        'id='*)\n" +
                "          echo -n \$(echo \${col:3}'|')\n" +
                "        ;;\n" +
                "      'width='*)\n" +
                "        echo -n \$(echo \${col:6})\n" +
                "        ;;\n" +
                "      'height='*)\n" +
                "        echo -n x\$(echo \${col:7})\n" +
                "        ;;\n" +
                "      'refreshRate='*)\n" +
                "        echo ' '\$(echo \${col:12} | cut -f1 -d '.')Hz\n" +
                "        ;;\n" +
                "      esac\n" +
                "    done\n" +
                "    echo -e '\\\\n'\n" +
                "  fi\n" +
                "done"
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
 * 获取电池信息(dumpsys)
 * @return Array<String>
 */
private fun getBatteryInfo(): Array<String> {
    val command =
        "dumpsys battery | while read row; do\n" + "  if [[ -n \$row ]]; then\n" + "    echo \$row\n" + "    echo -e '@'\n" + "  fi\n" + "done"
    return ShellUtils.execCommand(command, true, true).successMsg.let {
        it.takeIf { e -> e.isNotEmpty() }?.substring(0, it.length - 1)?.split("@")?.toTypedArray()
            ?: arrayOf()
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
    try {
        val contentValues = ContentValues(2)
        contentValues.put("name", key)
        contentValues.put("value", value)
        contentResolver.insert(Uri.parse("content://settings/system"), contentValues)
//        context.toast("apply $name Hz success!")
    } catch (e: Exception) {
        context.toast("apply $name Hz failed!")
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
        if (result == 0 && successMsg.isNotBlank()) return successMsg
    }
    ShellUtils.execCommand(
        "cat /sys/firmware/devicetree/base/firmware/android/serialno", false, true
    ).apply {
        if (result == 0 && successMsg.isNotBlank()) return successMsg
    }
    return "null"
}


/**
 * 获取GUID
 * /data/system/openid_config.xml
 */
val getGuid: String
    get() {
        ShellUtils.execCommand(
            "cat /data/system/openid_config.xml | sed  -n '3p'", true, true
        ).apply {
            return if (result == 1) "null"
            else successMsg.takeIf { e -> e.isNotBlank() }?.split("\"")?.get(3) ?: "null"
        }
    }


/**
 * 获取prop数据
 * @param key String
 */
fun getProp(key: String): String {
    return getProp(key, false)
}

fun getProp(key: String, root: Boolean): String = safeOf(default = "null") {
    ShellUtils.execCommand("getprop $key", root, true).let {
        if (it.result == 1) "null" else formatSpace(it.successMsg)
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
fun getFlashInfo(): String = safeOf(default = "null") {
    ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry", true, true).let {
        if (it.result == 1) return@safeOf "null" else return@safeOf formatSpace(it.successMsg)
    }
}

/**
 * 利用正则移除字符串前空格
 * @param string String
 */
fun formatSpace(string: String): String {
    val pattern = Pattern.compile("\\p{L}")
    val matcher = pattern.matcher(string)
    if (!matcher.find()) return string
    return string.substring(matcher.start())
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
 * @param string String 要复制的字符串
 */
fun Context.copyStr(string: String) {
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
    if (image == null) {
        result(null, true)
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
 * 从URI文件读取字符串
 * @param context Context
 * @param uri Uri
 * @return String
 */
fun readFromUri(context: Context, uri: Uri): String {
    val stringBuilder = StringBuilder()
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

/**
 * 获取文件路径
 * @param uri Uri 文件URI
 * @return String 文件Path
 */
fun getDocumentPath(context: Context, uri: Uri): String? {
    if (ContentResolver.SCHEME_CONTENT != uri.scheme) return "null"
    if (!DocumentsContract.isDocumentUri(context, uri)) return "null"
    val authority = when (uri.authority) {
        "com.android.externalstorage.documents" -> "ExternalStorageDocument"
        "com.android.providers.downloads.documents" -> "DownloadsDocument"
        "com.android.providers.media.documents" -> "MediaDocument"
        else -> "null"
    }
    when (authority) {
        "ExternalStorageDocument" -> {
            // ExternalStorageProvider
            val docId = DocumentsContract.getDocumentId(uri)
            val docArray = docId.split(":")
            val type = docArray[0]
            val dir = docArray[1]
            if ("primary" != type) return "null"
            return Environment.getExternalStorageDirectory().path + "/" + dir
        }

        "DownloadsDocument" -> {
            // DownloadsProvider
            val docId = DocumentsContract.getDocumentId(uri)
            if (TextUtils.isEmpty(docId)) return "null"
            return if (docId.startsWith("raw:")) {
                docId.replaceFirst("raw:", "")
            } else if (docId.contains("msf:")) {
                getMSFFile(context, uri)
            } else {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), ContentUris.parseId(uri)
                )
                getDataColumn(context, contentUri, null, null)
            }
        }

        "MediaDocument" -> {
            // MediaProvider
            val docId = DocumentsContract.getDocumentId(uri)
            val docArray = docId.split(":")
            val contentUri: Uri? = when (docArray[0]) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> null
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(docArray[1])
            return getDataColumn(context, contentUri!!, selection, selectionArgs)
        }
    }
    return "null"
}

/**
 * 获取文件数据列
 * @param context Context
 * @param uri Uri
 * @param selection String?
 * @param selectionArgs Array<String>?
 * @return String?
 */
fun getDataColumn(
    context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * 处理MSF类型
 * @param context Context
 * @param uri Uri
 * @return String?
 */
fun getMSFFile(context: Context, uri: Uri): String? {
    val dir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/LuckyTool/cache")
    if (!dir.exists()) dir.mkdirs()
    File(dir.path + "/.nomedia").createNewFile()
    val fileType = context.contentResolver.getType(uri)?.split("/")?.get(1)
    val fileName = SystemClock.uptimeMillis().toString() + "." + fileType
    val file = File(dir, fileName)
    val inputStream = context.contentResolver.openInputStream(uri)
    if (inputStream != null) return copyStreamToFile(inputStream, file)
    return null
}

/**
 * 复制文件
 * @param inputStream InputStream
 * @param outputFile File
 * @return String
 */
fun copyStreamToFile(inputStream: InputStream, outputFile: File): String {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
    return outputFile.path
}

/**
 * 读取文件输出字符串
 * @param file File
 * @return String?
 */
fun loadFile(file: File): String? {
    var fis: FileInputStream? = null
    var output: String? = null
    try {
        fis = FileInputStream(file)
        val buffer = ByteArray(4096)
        var len: Int
        val sb = StringBuilder()
        while (fis.read(buffer).also { len = it } != -1) {
            sb.append(String(buffer, 0, len))
        }
        output = sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        fis?.close()
    }
    return output
}

/**
 * 格式化Date
 * @param format String
 * @return String 格式
 */
fun formatDate(format: String): String {
    return formatDate(format, null, null)
}

/**
 * 格式化Date
 * @param format String 格式
 * @param param Any 要格式的对象
 * @return String
 */
fun formatDate(format: String, param: Any): String {
    return formatDate(format, param, null)
}

/**
 * 格式化Date
 * @param format String 格式
 * @param param Any? 要格式的对象
 * @param locale Locale? 区域
 * @return String
 */
fun formatDate(format: String, param: Any?, locale: Locale?): String {
    return SimpleDateFormat(format, locale ?: Locale.getDefault()).format(param ?: Date())
}

/**
 * 格式化Double
 * @param format String 格式
 * @param param Any 要格式化的对象
 * @return Double
 */
fun formatDouble(format: String, param: Any): Double {
    return Formatter().format(format, param).toString().toDoubleOrNull() ?: 0.0
}

fun isZh(context: Context): Boolean {
    val locale = context.resources.configuration.locales
    val language = locale[0].language
    return language.endsWith("zh")
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
                withIO {
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
        withIO {
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
fun Context.callFunc(bundle: Bundle?) {
    bundle?.apply {
        //自启功能相关
        if (getBoolean("fps_auto", false)) {
            val fpsMode = getInt("fps_mode", 1)
            val fpsCur = getInt("fps_cur", -1)
            if ((fpsMode == 2) && (fpsCur != -1)) ShellUtils.execCommand(
                "service call SurfaceFlinger 1035 i32 $fpsCur", true, true
            ).apply {
                if (result == 1) toast("force fps error!")
            }
        }
        //触控采样率相关
        if (getBoolean("touchSamplingRate", false)) {
            ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true, true)
                .apply {
                    if (result == 1) toast("touch sampling rate error!")
                }
        }
        //高亮度模式
        if (getBoolean("highBrightness", false)) {
            ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 1", true, true).apply {
                if (result == 1) toast("high brightness mode error!")
            }
        }
        //高性能模式
        if (getBoolean("highPerformance", false)) {
            ShellUtils.execCommand("settings put system high_performance_mode_on 1", true, true)
                .apply {
                    if (result == 1) toast("high performance mode error!")
                }
        }
        //全局DC模式
        if (getBoolean("globalDC", false)) {
            var oppoError = false
            var oplusError = false
            ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 1", true).apply {
                if (result == 1) oppoError = true
            }
            ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 1", true).apply {
                if (result == 1) oplusError = true
            }
            if (oppoError && oplusError) toast("global dc mode error!")
        }
        //快捷方式相关
        when (getString("Shortcut", "null")) {
            "lsposed" -> {
                ShellUtils.execCommand(
                    "am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",
                    true
                )
            }

            "oplusGames" -> ShellUtils.execCommand(
                "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true
            )

            "processManager" -> jumpRunningApp(this@callFunc)
            "chargingTest" -> jumpBatteryInfo(this@callFunc)
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
    val result =
        ShellUtils.execCommand("service call SurfaceFlinger 1034 i32 2", true, true).let {
            if (it.result == 1) return@safeOfFalse false
            else if (it.result == 0 && it.successMsg.isNotBlank()) it.successMsg
            else return@safeOfFalse false
        }
    val status = result.replace(" ", "").split("Parcel")[1].let {
        it.substring(1, it.length - 1)
    }.split("'")[0].let {
        it.substring(it.length - 1)
    }.toString()
    return when (status) {
        "0" -> false
        "1" -> true
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
fun Fragment.navigate(action: Int, title: CharSequence? = "Title") {
    findNavController().navigate(
        action,
        Bundle().apply {
            putCharSequence("title_label", title)
        })
}

/**
 * 跳转fragment传递参数
 * @receiver Fragment
 * @param action Int
 * @param bundle Bundle?
 */
fun Fragment.navigate(action: Int, bundle: Bundle?) {
    findNavController().navigate(action, bundle)
}

/**
 * 获取设备参数
 * @return JSONObject?
 */
fun getDevicesConfig(): JSONObject? {
    val json =
        ShellUtils.execCommand("cat /odm/etc/devices_config/devices_config.json", false, true)
            .let {
                if (it.result == 1) null
                else if (it.successMsg.isBlank()) null
                else it.successMsg
            }
    return json?.let { JSONObject(it) }
}

/**
 * 是否为串联电池
 */
val isSeriesDualBattery
    get() : Boolean? = safeOfNull {
        getDevicesConfig()?.getJSONObject("charge")?.getBoolean("series_dual_battery_support")
    }

/**
 * 是否为并联电池
 */
val isParallelDualBattery
    get() : Boolean? = safeOfNull {
        getDevicesConfig()?.getJSONObject("charge")?.getBoolean("parallel_dual_battery_support")
    }

val isVBatDeviation
    get() : Boolean? = safeOfNull {
        getDevicesConfig()?.getJSONObject("charge")?.getBoolean("qg_vbat_deviation_support")
    }