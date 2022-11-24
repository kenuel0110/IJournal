package com.twostrangerteam.ijournal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFB()
        tabLayoutShedule.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                initRV(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        initRV(0)
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
                                var local_item = sh
                                adapter.add(SheduleItem(local_item))
                                binding.rvSheduleList.layoutManager = LinearLayoutManager(this@SheduleActivity)
                                binding.rvSheduleList.adapter = adapter
                                adapter.notifyDataSetChanged()
                                list_mon.add(sh)
                            }
                            "Tue" -> {
                                list_tue.add(sh)
                            }
                            "Wed" -> {
                                list_wed.add(sh)
                            }
                            "Thu" -> {
                                list_thu.add(sh)
                            }
                            "Fri" -> {
                                list_fri.add(sh)
                            }
                            "Sat" -> {
                                list_sat.add(sh)
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
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRV(tab_string: Int) {
        adapter.clear()
        when(tab_string){
        0 -> {
            list_mon.forEach {
                var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                adapter.add(SheduleItem(local_item))
                binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                binding.rvSheduleList.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
        1 -> {
            list_tue.forEach {
                var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                adapter.add(SheduleItem(local_item))
                binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                binding.rvSheduleList.adapter = adapter
                adapter.notifyDataSetChanged()
                }
            }

        2 -> {
                list_wed.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                    binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                    binding.rvSheduleList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            3 -> {
                list_thu.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                    binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                    binding.rvSheduleList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }


            4 -> {
                list_fri.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                    binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                    binding.rvSheduleList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            5 -> {
                list_sat.forEach {
                    var local_item = SheduleModel(it.dayOfWeek, it.name, it.teacher, it.type)
                    adapter.add(SheduleItem(local_item))
                    binding.rvSheduleList.layoutManager = LinearLayoutManager(this)
                    binding.rvSheduleList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }
}