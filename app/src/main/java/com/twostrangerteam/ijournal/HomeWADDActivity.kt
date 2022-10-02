package com.twostrangerteam.ijournal

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.twostrangerteam.ijournal.classes.HomeWork
import com.twostrangerteam.ijournal.classes.User
import com.twostrangerteam.ijournal.databinding.ActivityHomeWaddactivityBinding
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import javax.xml.datatype.DatatypeConstants.MONTHS

import java.text.SimpleDateFormat


class HomeWADDActivity : AppCompatActivity() {
    private var lesson_DB = ""
    private var lessons_list: ArrayList<String> = arrayListOf("Экология", "Информационные технологии", "Интернет технологии", "Культура речи", "Английский", "Математика", "Физ-ра", "Введение в профессию", "История", "Проектная деятельность")
    lateinit var binding: ActivityHomeWaddactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeWaddactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()
        initDate()

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnConf.setOnClickListener {
            add2database()
        }

        binding.btnDataPicker.setOnClickListener {
            open_datapicker()
        }
    }

    private fun initDate() {
        val simpleDateFormat = SimpleDateFormat("dd.M.yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding.teDataStart.setText(currentDateAndTime)
        binding.teDataStart.isEnabled = false
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.lessons, R.layout.spinner_list) as SpinnerAdapter
        binding.spinnerLessons.adapter = adapter

        binding.spinnerLessons.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                binding.teDataEnd.setText("$dayOfMonth.${month+1}.$year")
            }, year, month, day
        )
        dpd.show()
    }

    private fun add2database() {
        if (binding.teTitle.text.isEmpty() || binding.teHomework.text!!.isEmpty() ||
            lesson_DB == "" || binding.teDataEnd.text.isEmpty()
        ) {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
        }
        else{
            val uid = UUID.randomUUID().toString()
            val ref = FirebaseDatabase.getInstance().getReference("/home_work/$uid")

            val hw = HomeWork(uid, binding.teTitle.text.toString(), lesson_DB, binding.teHomework.text.toString(), binding.teDataStart.text.toString(), binding.teDataEnd.text.toString())

            ref.setValue(hw).addOnSuccessListener {
                Toast.makeText(this, "Домашка добавлена", Toast.LENGTH_SHORT).show()
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