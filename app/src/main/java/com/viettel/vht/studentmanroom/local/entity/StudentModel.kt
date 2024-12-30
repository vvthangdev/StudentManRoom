package com.viettel.vht.studentmanroom.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val studentId: String
)
