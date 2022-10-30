package com.demo.provider.db

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = student_id)
    var id: Int?,
    @ColumnInfo(name = student_name)
    var name: String,
    @ColumnInfo(name = student_age)
    var age: Int
) {
    constructor() : this(null, "", 0)


    companion object {
        fun fromContentValue(contentValue: ContentValues?): Student {
            val student = Student()
            contentValue?.apply {
                if (containsKey(student_name)) {
                    student.name = getAsString(student_name)
                }
                if (containsKey(student_age)) {
                    student.age = getAsInteger(student_age)
                }
                if (containsKey(student_id)) {
                    student.id = getAsInteger(student_id)
                }
            }

            return student
        }

        const val table_name = "students"
        const val student_name = "student_name"
        const val student_age = "student_age"
        const val student_id = "student_id"

    }
}