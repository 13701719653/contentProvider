package com.demo.provider

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.demo.provider.databinding.ActivityMainBinding
import com.demo.provider.db.MyDatabase
import com.demo.provider.db.Student
import com.demo.provider.provider.MyProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding :ActivityMainBinding
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        CoroutineScope(Dispatchers.IO).launch {
            initDB()
                contentResolver.query(Uri.parse("content://${MyProvider.AUTHORITY}/${Student.table_name}"),null,null,null)?.apply {
                var result = StringBuffer()
                while(moveToNext()){
                    val id = getInt(getColumnIndex(Student.student_id))
                    val name = getString(getColumnIndex(Student.student_name))
                    val age = getInt(getColumnIndex(Student.student_age))
                        result.
                        append(Student.student_id).append("=").append(id).
                        append(Student.student_name).append("=").append(name).
                        append(Student.student_age).append("=").append(age)

                }
                withContext(Dispatchers.Main){
                    binding.tv.text = result
                }

            }


        }

    }

    private fun initDB(){
        val dao = MyDatabase.getInstance(this).studentDao()
        val cursor = dao.query()
        if(cursor==null||!cursor.moveToNext()){
            dao.insert(Student(null,"xwg",11))
        }
    }
}