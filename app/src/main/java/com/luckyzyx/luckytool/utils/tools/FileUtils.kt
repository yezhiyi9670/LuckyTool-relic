@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.os.Environment
import java.io.*

class FileUtils {
    /**
     * 获取Download目录
     * @return File
     */
    private fun getDownDirs(): File {
        val dir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * 写入字符串到文件
     * @param str String
     * @param fileName String
     */
    fun writeToFile(str: String, fileName: String): Boolean {
        // 每次写入时，都换行写
        val strContent = str + "\r\n\n"
        //生成文件夹之后，再生成文件，不然会出错
        val file: File?
        try {
            file = File(getDownDirs(), fileName)
            if (!file.exists()) {
                file.createNewFile()
            } else {
                //清空文本内容
                val fileWriter = FileWriter(file)
                fileWriter.write("")
                fileWriter.flush()
                fileWriter.close()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 遍历数组写入文件
     * @param array Array<*>
     * @param fileName String
     * @return Boolean
     */
    fun writeArrayToFile(array: ArrayList<*>, fileName: String): Boolean {
        var str = ""
        array.forEach {
            str += it.toString() + "\r\n\n"
        }
        val file: File?
        try {
            file = File(getDownDirs(), fileName)
            if (!file.exists()) {
                file.createNewFile()
            } else {
                //清空文本内容
                val fileWriter = FileWriter(file)
                fileWriter.write("")
                fileWriter.flush()
                fileWriter.close()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(str.toByteArray())
            raf.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}