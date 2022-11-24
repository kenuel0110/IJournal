package com.twostrangerteam.ijournal

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.tabs.TabLayout
import com.twostrangerteam.ijournal.classes.RingBallItem
import com.twostrangerteam.ijournal.classes.RingBallModel
import com.twostrangerteam.ijournal.databinding.ActivityBallListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_ball_list.*


class BallListActivity : AppCompatActivity() {
    lateinit var binding: ActivityBallListBinding

    private var adapter = GroupAdapter<ViewHolder>()

    private var items_kolh: List<RingBallModel> = listOf<RingBallModel>(
        RingBallModel("1", "8:30 - 9:15", "5 мин "),
        RingBallModel(" ", "9:20 - 10:05", "10 мин"),
        RingBallModel("2", "10:15 - 11:00", "5 мин "),
        RingBallModel(" ", "11:05 - 11:50", "30 мин"),
        RingBallModel("3", "12:20 - 13:05", "5 мин "),
        RingBallModel(" ", "13:10 - 13:55", "10 мин"),
        RingBallModel("4", "14:05 - 14:50", "5 мин "),
        RingBallModel(" ", "14:55 - 15:40", "      ")
    )

    private var items_main: List<RingBallModel> = listOf<RingBallModel>(
        RingBallModel("1", "8:30 - 9:15", "5 мин "),
        RingBallModel(" ", "9:20 - 10:05", "10 мин"),
        RingBallModel("2", "10:15 - 11:00", "5 мин "),
        RingBallModel(" ", "11:05 - 11:50", "40 мин"),
        RingBallModel("3", "12:30 - 13:15", "5 мин "),
        RingBallModel(" ", "13:20 - 14:05", "10 мин"),
        RingBallModel("4", "14:15 - 15:00", "5 мин "),
        RingBallModel(" ", "15:05 - 15:50", "      ")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBallListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (binding.rvBallList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvBallList.layoutManager = LinearLayoutManager(this)
        binding.rvBallList.adapter = adapter

        initRV(0)
        tabLayoutBall.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                initRV(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


    }

    private fun initRV(tab_string: Int) {
        adapter.clear()
        when(tab_string){
            0 -> {
                items_main.forEach {
                    var local_item = RingBallModel(it.lesson_num, it.timeStart_End, it.breakTime)
                    adapter.add(RingBallItem(local_item))
                    adapter.notifyItemRangeChanged(0, binding.rvBallList.size)
                }

            }
            1 -> {
                    items_kolh.forEach {
                        var local_item = RingBallModel(it.lesson_num, it.timeStart_End, it.breakTime)
                        adapter.add(RingBallItem(local_item))
                        adapter.notifyItemRangeChanged(0, binding.rvBallList.size)
                    }
            }
        }
    }
}