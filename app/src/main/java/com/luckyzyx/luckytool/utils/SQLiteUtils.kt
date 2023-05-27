package com.luckyzyx.luckytool.utils

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

@Suppress("unused")
object SQLiteUtils {

    const val readOnly = SQLiteDatabase.OPEN_READONLY
    const val readWrite = SQLiteDatabase.OPEN_READWRITE

    /**
     * 打开数据库
     * @param dbPath String 数据库路径
     * @param mode Int 数据库访问模式
     * @return SQLiteDatabase?
     */
    fun openDataBase(dbPath: String, mode: Int): SQLiteDatabase? {
        return SQLiteDatabase.openDatabase(dbPath, null, mode)
    }


    /**
     * 打开或创建数据库
     * @param dbPath String 数据库路径
     * @return SQLiteDatabase?
     */
    fun openOrCreateDataBase(dbPath: String): SQLiteDatabase? {
        return SQLiteDatabase.openOrCreateDatabase(dbPath, null)
    }

    /**
     * 获取表数据
     * @receiver SQLiteDatabase?
     * @param table String
     * @return Cursor?
     */
    fun SQLiteDatabase?.getTableData(table: String): Cursor? {
        return this?.query(table, null, null, null, null, null, null)
    }

}