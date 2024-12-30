package com.viettel.vht.studentmanroom

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.viettel.vht.studentmanroom.databinding.ActivityMainBinding
import com.viettel.vht.studentmanroom.local.StudentDatabase
import com.viettel.vht.studentmanroom.local.entity.StudentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentDatabase: StudentDatabase
    private val students = mutableListOf<StudentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentDatabase = StudentDatabase.getInstance(this)

        registerForContextMenu(binding.listViewStudents)

        binding.buttonAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_STUDENT)
        }

        loadStudents()
    }

    private fun loadStudents() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dbStudents = studentDatabase.studentDao().getAllStudents()
            withContext(Dispatchers.Main) {
                students.clear()
                students.addAll(dbStudents)
                binding.listViewStudents.adapter =
                    ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, students.map { "${it.studentName} (${it.studentId})" })
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val student = students[info.position]

        return when (item.itemId) {
            R.id.menu_edit -> {
                editStudent(student)
                true
            }
            R.id.menu_delete -> {
                deleteStudent(student)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun editStudent(student: StudentModel) {
        val intent = Intent(this, AddStudentActivity::class.java).apply {
            putExtra("EXTRA_STUDENT_NAME", student.studentName)
            putExtra("EXTRA_STUDENT_ID", student.studentId)
            putExtra("EXTRA_STUDENT_ID_DB", student.id)
        }
        startActivityForResult(intent, REQUEST_EDIT_STUDENT)
    }

    private fun deleteStudent(student: StudentModel) {
        AlertDialog.Builder(this)
            .setTitle("Xóa học sinh")
            .setMessage("Bạn có chắc chắn muốn xóa học sinh này?")
            .setPositiveButton("Có") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    studentDatabase.studentDao().delete(student)
                    loadStudents()
                }
            }
            .setNegativeButton("Không", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val studentName = data.getStringExtra("EXTRA_STUDENT_NAME") ?: return
            val studentId = data.getStringExtra("EXTRA_STUDENT_ID") ?: return
            val studentIdDb = data.getIntExtra("EXTRA_STUDENT_ID_DB", -1)

            if (requestCode == REQUEST_ADD_STUDENT) {
                val newStudent = StudentModel(studentName = studentName, studentId = studentId)
                lifecycleScope.launch(Dispatchers.IO) {
                    studentDatabase.studentDao().insert(newStudent)
                    loadStudents()
                }
            } else if (requestCode == REQUEST_EDIT_STUDENT && studentIdDb != -1) {
                val updatedStudent = StudentModel(id = studentIdDb, studentName = studentName, studentId = studentId)
                lifecycleScope.launch(Dispatchers.IO) {
                    studentDatabase.studentDao().update(updatedStudent)
                    loadStudents()
                }
            }
        }
    }

    companion object {
        const val REQUEST_ADD_STUDENT = 1
        const val REQUEST_EDIT_STUDENT = 2
    }
}
