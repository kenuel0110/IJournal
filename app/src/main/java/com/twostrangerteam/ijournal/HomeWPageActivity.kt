package com.twostrangerteam.ijournal

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.twostrangerteam.ijournal.classes.HomeWorkItem
import com.twostrangerteam.ijournal.classes.HomeWorkModel
import com.twostrangerteam.ijournal.classes.User
import com.twostrangerteam.ijournal.databinding.ActivityHomeWpageBinding
import com.twostrangerteam.ijournal.fragments.FragmentHomeWork.Companion.HOMEWORK_KEY
import com.twostrangerteam.ijournal.joinus.LoginActivity
import java.util.*

class HomeWPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeWpageBinding

    private var uuid_hw = ""
    private var title_hw = ""
    private var homeWork = ""
    private var lesson = ""
    private var startData = ""
    private var endData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeWpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uuid_hw = intent.getStringExtra(HOMEWORK_KEY).toString()



        initData(uuid_hw)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            try2ShareIt()
        }

        binding.btnDelete.setOnClickListener {
            deleteItem(uuid_hw)
        }

        binding.teTitleHw.setOnClickListener {
            dataChange("Изменение загаловка", title_hw, "Введите новый заголовок", InputType.TYPE_CLASS_TEXT, "title")
        }

        binding.tvHwHw.setOnClickListener {
            dataChange("Изменение домашней работы", homeWork, "Введите новый заголовок", InputType.TYPE_CLASS_TEXT, "homeWork")
        }

        binding.teStartHw.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val dpd = DatePickerDialog(this,
                { view, year, month, dayOfMonth -> // Display Selected date in textbox
//                    binding.teStartHw.setText("$dayOfMonth.${month+1}.$year")
                    startData = "$dayOfMonth.${month+1}.$year"
                    changeHWData()
                }, year, month, day
            )
            dpd.show()
        }

        binding.teEndHw.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val dpd = DatePickerDialog(this,
                { view, year, month, dayOfMonth -> // Display Selected date in textbox
//                    binding.teStartHw.setText("$dayOfMonth.${month+1}.$year")
                    endData = "$dayOfMonth.${month+1}.$year"
                    changeHWData()
                }, year, month, day
            )
            dpd.show()
        }
    }

    private fun dataChange(title: String, value: String, hint: String, type: Int, name_v: String) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        input.setHint(hint)
        input.setText(value)
        input.inputType = type
        builder.setView(input)
        builder.setPositiveButton("Изменить", DialogInterface.OnClickListener { dialog, which ->
            when(name_v){
                "title"->{
                    title_hw = input.text.toString()
                }
                "homeWork"->{
                    homeWork = input.text.toString()
                }
            }
            if (value != input.text.toString()) changeHWData()
        })
        builder.setNegativeButton("Отменить", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun changeHWData() {
        val ref = FirebaseDatabase.getInstance().getReference("/home_work/$uuid_hw")

        val mRefHome = FirebaseDatabase.getInstance().getReference("/home_work/")
        mRefHome.child(uuid_hw.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val hw = HomeWorkModel(
                    uuid_hw,
                    title_hw,
                    lesson,
                    homeWork,
                    startData,
                    endData)

                ref.setValue(hw).addOnSuccessListener{
                    Toast.makeText(this, "Данные изменены", Toast.LENGTH_SHORT).show()
                    initData(uuid_hw)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun deleteItem(uuid_hw: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Вы уверены, что хотите удалить задание?")
        builder.setMessage("Это действие нельзя отменить")
        builder.setPositiveButton("Да",{dialogInterface, i ->
            val db = FirebaseDatabase.getInstance().getReference("/home_work/").child(uuid_hw)
            val delete = db.removeValue()

            delete.addOnSuccessListener {
                Toast.makeText(this, "Задание удаленно", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Нет",{dialog, i -> })
        builder.show()
    }

    private fun try2ShareIt() {
        val sendIntent: Intent = Intent().apply{
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
            "Заголовок: ${binding.teTitleHw.text.toString()}\n" +
                    "Описание: ${binding.tvHwHw.text.toString()}\n" +
                    "Урок: ${binding.tvLessonHw.text.toString()}\n" +
                    "Дата сдачи задания: ${binding.teEndHw.text.toString()}"
                )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun initData(uuidHw: String) {
        val mRefHW = FirebaseDatabase.getInstance().getReference("/home_work/")
        mRefHW.child(uuidHw).get().addOnSuccessListener {
            if (it.exists()) {
                 title_hw = it.child("title").value.toString()
                 homeWork = it.child("quest").value.toString()
                 lesson = it.child("lesson").value.toString()
                 startData = it.child("data_start").value.toString()
                 endData = it.child("data_end").value.toString()
            }

            binding.teTitleHw.text = title_hw
            binding.tvHwHw.text =homeWork
            binding.tvLessonHw.text =lesson
            binding.teStartHw.text = startData
            binding.teEndHw.text = endData
        }
    }
}
