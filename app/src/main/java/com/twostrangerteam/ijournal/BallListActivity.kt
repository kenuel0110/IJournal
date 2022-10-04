package com.twostrangerteam.ijournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twostrangerteam.ijournal.classes.HomeWorkItem
import com.twostrangerteam.ijournal.classes.RingBallItem
import com.twostrangerteam.ijournal.databinding.ActivityBallListBinding
import com.twostrangerteam.ijournal.databinding.ActivityHomeWaddactivityBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class BallListActivity : AppCompatActivity() {
    lateinit var binding: ActivityBallListBinding

    private var items: List<RingBallItem> = listOf<RingBallItem>(
        RingBallItem("1", "8:30 - 9:15", "5 мин"),
        RingBallItem("", "9:20 - 10:05", "10 мин"),
        RingBallItem("2", "10:15 - 11:00", "5 мин"),
        RingBallItem("", "11:05 - 11:50", "30 мин"),
        RingBallItem("3", "12:20 - 13:05", "5 мин"),
        RingBallItem("", "13:10 - 13:55", "10 мин"),
        RingBallItem("4", "14:05 - 14:50", "5 мин"),
        RingBallItem("", "14:55 - 15:40", "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBallListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRV()


    }

    private fun initRV() {
        val adapter = GroupAdapter<ViewHolder>()
        items.forEach{
            var local_item = RingBallItem(it.lesson_num, it.timeStart_End, it.breakTime)
            adapter.add(local_item)
        }
    }
}