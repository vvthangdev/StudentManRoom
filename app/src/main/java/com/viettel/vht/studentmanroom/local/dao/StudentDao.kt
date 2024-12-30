package com.viettel.vht.studentmanroom.local.dao

import androidx.room.*
import com.viettel.vht.studentmanroom.local.entity.StudentModel

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentModel)

    @Update
    suspend fun update(student: StudentModel)

    @Delete
    suspend fun delete(student: StudentModel)

    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<StudentModel>
}
