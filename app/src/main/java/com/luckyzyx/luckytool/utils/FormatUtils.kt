package com.luckyzyx.luckytool.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Formatter
import java.util.Locale
import java.util.regex.Pattern


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
 * 格式化数据大小
 * @param str String
 * @return String
 */
fun formatDataSize(str: String): String {
    val int = str.toFloatOrNull() ?: return str
    return if (int >= (1024 * 1024 * 1024)) {
        DecimalFormat("0.00").format(int / (1024 * 1024 * 1024)).toString() + " GB"
    } else if (int >= (1024 * 1024)) {
        DecimalFormat("0.00").format(int / (1024 * 1024)).toString() + " MB"
    } else if (int >= (1024)) {
        DecimalFormat("0.00").format(int / (1024)).toString() + " KB"
    } else "$int B"
}