package com.twostrangerteam.ijournal

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.twostrangerteam.ijournal.classes.Gradle
import com.twostrangerteam.ijournal.classes.HomeWork
import com.twostrangerteam.ijournal.databinding.ActivityGradeItemBinding
import com.twostrangerteam.ijournal.databinding.ActivityHomeWaddactivityBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GradeItemEditActivity : AppCompatActivity() {
    private var lesson_DB = ""
    private var lessons_list: ArrayList<String> = arrayListOf("Экология", "Информационные технологии", "Интернет технологии", "Культура речи", "Английский", "Математика", "Физ-ра", "Введение в профессию", "История", "Проектная деятельность")
    lateinit var binding: ActivityGradeItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()
        initDate()

        binding.btnCancelGradle.setOnClickListener {
            finish()
        }

        binding.btnConfGradle.setOnClickListener {
            add2database()
        }

        binding.btnDataPickerGradle.setOnClickListener {
            open_datapicker()
        }
    }

    private fun initDate() {
        val simpleDateFormat = SimpleDateFormat("dd.M.yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding.teDataGradle.setText(currentDateAndTime)
        binding.teDataGradle.isEnabled = false
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.lessons, R.layout.spinner_list) as SpinnerAdapter
        binding.spinnerLessonsGradle.adapter = adapter

        binding.spinnerLessonsGradle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lesson_DB = lessons_list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun open_datapicker() {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]


        val dpd = DatePickerDialog(this,
            { view, year, month, dayOfMonth -> // Display Selected date in textbox
                binding.teDataGradle.setText("$dayOfMonth.${month+1}.$year")
            }, year, month, day
        )
        dpd.show()
    }

    private fun add2database() {
        if (binding.teGradleTitle.text.isEmpty() || binding.teDataGradle.text!!.isEmpty() ||
            lesson_DB == ""
        ) {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
        }
        else{
            if (binding.teGradleTitle.text.toString().toInt() < 2 || binding.teGradleTitle.text.toString().toInt() > 5){
                Toast.makeText(this, "Введите корректную оценку", Toast.LENGTH_SHORT).show()
            }
            else{
            val uid = UUID.randomUUID().toString()
            val user_uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference("/grades/$uid")

            val gr = Gradle(uid,user_uid.toString(), binding.teGradleTitle.text.toString(), lesson_DB, binding.teDataGradle.text.toString())

            ref.setValue(gr).addOnSuccessListener {
                Toast.makeText(this, "Оценка добавлена", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}