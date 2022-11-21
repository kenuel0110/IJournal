package com.twostrangerteam.ijournal.classes

import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_shedule.view.*

class SheduleItem (val shedule: SheduleModel): Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tv_name.text = shedule.name
            viewHolder.itemView.tv_teacher.text = shedule.teacher
            viewHolder.itemView.tv_type.text = shedule.type
        }

        override fun getLayout(): Int {
            return R.layout.item_shedule
        }
    }