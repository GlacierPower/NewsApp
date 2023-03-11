package com.newsapp.util

import android.content.Context
import java.io.File

object DatabaseUtil {

    @Throws(Exception::class)
    fun getRoomDatabaseSize(context: Context, dbName: String): Long {
        val dbFolderPath = context.filesDir.absolutePath.replace("files", "databases")
        val dbFile = File("$dbFolderPath/$dbName")
        if (!dbFile.exists()) throw Exception("${dbFile.absolutePath} doesn't exist")

        return dbFile.length()
    }
}