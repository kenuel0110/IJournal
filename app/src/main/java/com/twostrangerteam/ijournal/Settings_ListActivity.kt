package com.twostrangerteam.ijournal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import com.twostrangerteam.ijournal.databinding.ActivitySettingsListBinding

class Settings_ListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инит биндинга
        binding = ActivitySettingsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSettingsUpdate.setOnClickListener {
            val url = "https://ijournal.page.link/telegram_updates"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}