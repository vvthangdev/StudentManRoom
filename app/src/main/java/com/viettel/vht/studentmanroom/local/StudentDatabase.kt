package com.viettel.vht.studentmanroom.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viettel.vht.studentmanroom.local.dao.StudentDao
import com.viettel.vht.studentmanroom.local.entity.StudentModel

@Database(entities = [StudentModel::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile private var instance: StudentDatabase? = null

        fun getInstance(context: Context): StudentDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                ).build().also { instance = it }
            }
    }
}

