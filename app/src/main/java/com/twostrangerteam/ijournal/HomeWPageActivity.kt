package com.twostrangerteam.ijournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.twostrangerteam.ijournal.databinding.ActivityHomeWpageBinding
import com.twostrangerteam.ijournal.fragments.FragmentHomeWork.Companion.HOMEWORK_KEY
import com.twostrangerteam.ijournal.joinus.LoginActivity

class HomeWPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeWpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeWpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var uuid_hw = intent.getStringExtra(HOMEWORK_KEY).toString()

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
                binding.teTitleHw.text = it.child("title").value.toString()
                binding.tvHwHw.text = it.child("quest").value.toString()
                binding.tvLessonHw.text = it.child("lesson").value.toString()
                binding.teStartHw.text = it.child("data_start").value.toString()
                binding.teEndHw.text = it.child("data_end").value.toString()
            }
        }
    }
}