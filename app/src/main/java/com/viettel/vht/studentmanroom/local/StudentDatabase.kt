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
        @Volatile
        private var instance: StudentDatabase? = null

        fun getInstance(context: Context): StudentDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    getDatabasePath(context)
                ).build().also { instance = it }
            }

        /**
         * Tạo đường dẫn tới thư mục riêng của ứng dụng để lưu trữ cơ sở dữ liệu.
         * Không yêu cầu quyền WRITE_EXTERNAL_STORAGE.
         */
        private fun getDatabasePath(context: Context): String {
            return context.getExternalFilesDir(null)?.absolutePath + "/student_database"
        }
    }
}

