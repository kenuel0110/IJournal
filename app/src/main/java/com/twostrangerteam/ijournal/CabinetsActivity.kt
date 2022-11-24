package com.twostrangerteam.ijournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twostrangerteam.ijournal.classes.CabinetItem
import com.twostrangerteam.ijournal.classes.CabinetModel
import com.twostrangerteam.ijournal.classes.HomeWorkItem
import com.twostrangerteam.ijournal.classes.HomeWorkModel
import com.twostrangerteam.ijournal.databinding.ActivityCabinetsBinding
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import com.twostrangerteam.ijournal.fragments.FragmentHomeWork
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class CabinetsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCabinetsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCabinetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mRefProducts = FirebaseDatabase.getInstance().getReference("/cabinets/").orderByChild("num_cab")
        mRefProducts.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //адаптер для рицайклвью
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    val ca = it.getValue(CabinetModel::class.java)
                    //добавление заполненого шаблона в рицайклвью
                    if(ca != null){
                        adapter.add(CabinetItem(ca))
                    }
                }
                binding.rvCabinets.layoutManager = LinearLayoutManager(this@CabinetsActivity)

                if(binding.rvCabinets.getAdapter()?.getItemCount() == 0){
                    adapter.add(CabinetItem(CabinetModel("", "Список пуст")))
                }

                //действие по нажатию
                adapter.setOnItemClickListener { item, view ->

                }
                //подсоединение адаптера к рицайклвью
                binding.rvCabinets.adapter = adapter
                adapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}