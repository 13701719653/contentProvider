package com.demo.provider.db

import android.database.Cursor
import androidx.room.*

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(student: Student): Long

    @Query("select * from students where student_name=:name")
    fun selectByName(name: String): Cursor?

    @Query("select * from students")
    fun query(): Cursor?


    @Query("delete from students where student_name=:name")
    fun deleteByName(name: String): Int


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update( student: Student): Int

}