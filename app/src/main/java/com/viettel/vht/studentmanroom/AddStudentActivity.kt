package com.viettel.vht.studentmanroom

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.viettel.vht.studentmanroom.databinding.ActivityAddStudentBinding

class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val studentName = intent.getStringExtra("EXTRA_STUDENT_NAME")
        val studentId = intent.getStringExtra("EXTRA_STUDENT_ID")
        val studentIdDb = intent.getIntExtra("EXTRA_STUDENT_ID_DB", -1)

        if (studentName != null && studentId != null) {
            binding.editTextStudentName.setText(studentName)
            binding.editTextStudentId.setText(studentId)
        }

        binding.buttonSave.setOnClickListener {
            val resultIntent = intent.apply {
                putExtra("EXTRA_STUDENT_NAME", binding.editTextStudentName.text.toString())
                putExtra("EXTRA_STUDENT_ID", binding.editTextStudentId.text.toString())
                putExtra("EXTRA_STUDENT_ID_DB", studentIdDb)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
