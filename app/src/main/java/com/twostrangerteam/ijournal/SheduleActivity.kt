package com.twostrangerteam.ijournal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twostrangerteam.ijournal.classes.*
import com.twostrangerteam.ijournal.databinding.ActivitySettingsListBinding
import com.twostrangerteam.ijournal.databinding.ActivitySheduleBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ball_list.*
import kotlinx.android.synthetic.main.activity_shedule.*

class SheduleActivity : AppCompatActivity() {
    lateinit var binding: ActivitySheduleBinding

    private var list_mon: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()
    private var list_tue: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()
    private var list_wed: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()
    private var list_thu: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()
    private var list_fri: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()
    private var list_sat: ArrayList<SheduleModel> = arrayListOf<SheduleModel>()

    private var adapter = GroupAdapter<ViewHolder>()
    private var tabNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
        binding.rvSheduleList.adapter = adapter

        binding.swipetorefreshShed.isRefreshing = true
        getDataFB()
        initRV()
        binding.swipetorefreshShed.isRefreshing = false

        tabLayoutShedule.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabNum = tab!!.position
                initRV()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.swipetorefreshShed.setOnRefreshListener {
            clearLists()
            getDataFB()
            initRV()
            binding.swipetorefreshShed.isRefreshing = false
        }
    }

    private fun getDataFB() {
        val mRefProducts = FirebaseDatabase.getInstance().getReference("/shedule/").orderByChild("dayOfWeek")
        mRefProducts.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val sh = it.getValue(SheduleModel::class.java)
                    if(sh != null){
                        when(sh.dayOfWeek){
                            "Mon" -> {
                                list_mon.add(sh)
                                if (tabNum == 0){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                            "Tue" -> {
                                list_tue.add(sh)
                                if (tabNum == 1){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                            "Wed" -> {
                                list_wed.add(sh)
                                if (tabNum == 2){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                            "Thu" -> {
                                list_thu.add(sh)
                                if (tabNum == 3){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                            "Fri" -> {
                                list_fri.add(sh)
                                if (tabNum == 4){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                            "Sat" -> {
                                list_sat.add(sh)
                                if (tabNum == 5){
                                    var local_item = sh
                                    adapter.add(SheduleItem(local_item))
                                }
                            }
                        }

                    }
                    else{
                        Toast.makeText(this@SheduleActivity, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        adapter.notifyDataSetChanged()
    }

    private fun clearLists() {
        adapter.clear()
        list_mon.clear()
        list_tue.clear()
        list_wed.clear()
        list_thu.clear()
        list_fri.clear()
        list_sat.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRV() {
        adapter.clear()
        when(tabNum){
        0 -> {
            list_mon.forEach {
                var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                adapter.add(SheduleItem(local_item))
            }
        }
        1 -> {
                list_tue.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                    }
        }

        2 -> {
                list_wed.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                }
            }


            3 -> {
                list_thu.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                }
            }


            4 -> {
                list_fri.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                }
            }

            5 -> {
                list_sat.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                }
            }
        }
        adapter.notifyDataSetChanged()
        //Toast.makeText(this, adapter.itemCount.toString(), Toast.LENGTH_SHORT).show()
    }
}