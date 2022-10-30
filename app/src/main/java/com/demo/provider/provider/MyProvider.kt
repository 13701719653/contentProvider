package com.demo.provider.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.demo.provider.db.MyDatabase
import com.demo.provider.db.Student
import com.demo.provider.db.StudentDao
import java.lang.IllegalArgumentException

class MyProvider : ContentProvider() {
    lateinit var studentDao: StudentDao
    override fun onCreate(): Boolean {
        context?.let {
            uriMatcher.addURI(AUTHORITY, "students", studentDir)
            uriMatcher.addURI(AUTHORITY, "students/#", studentItem)
            studentDao = MyDatabase.getInstance(it).studentDao()
            return true
        }
        return false
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val studentDir = 0
    private val studentItem = 1
    companion object{
        val AUTHORITY = "com.demo.providerdemo.provider"
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = when (uriMatcher.match(uri)) {
            studentDir -> studentDao.query()
            studentItem -> studentDao.selectByName(uri.pathSegments[1])
            else -> null
        }
        cursor?.setNotificationUri(context?.contentResolver,uri)
         return cursor
    }

    override fun getType(uri: Uri): String? {
        val packageName = context?.packageName ?: ""
        val type = when (uriMatcher.match(uri)) {
            studentDir -> "vnd.android.cursor.dir/vnd.${packageName}.provider.students"
            studentItem -> "vnd.android.cursor.item/vnd.${packageName}.provider.students"
            else -> null
        }
        return type
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val type = uriMatcher.match(uri)

        val uri = when (type) {
            studentDir -> {
                val id = studentDao.insert(Student.fromContentValue(values))
                ContentUris.withAppendedId(uri, id)
            }
            studentItem -> throw  IllegalArgumentException("不能插name")
            else -> throw IllegalArgumentException("error uri${uri}")
        }
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            studentDir -> studentDao.deleteByName(selectionArgs?.get(0) ?: "")
            studentItem -> throw  IllegalArgumentException("无效id")
            else -> throw IllegalArgumentException("error uri:${uri}")
        }
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when (uriMatcher.match(uri)) {

            studentDir -> {
                val count = studentDao.update(Student.fromContentValue(values))
              context?.contentResolver?.notifyChange(uri, null)
                return count
            }

             studentItem-> throw IllegalArgumentException("IllegalArgumentException ID")
            else -> throw IllegalArgumentException("unknow uri ${uri}")
        }
    }
}