package com.twostrangerteam.ijournal.classes

import com.squareup.picasso.Picasso
import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.home_work_item.view.*

class HomeWorkItem(val hw: HomeWorkModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_title.text = hw.title
        viewHolder.itemView.tv_lesson.text = hw.lesson
        viewHolder.itemView.tv_data_start.text = hw.data_start
        viewHolder.itemView.data_end.text = hw.data_end
    }

    override fun getLayout(): Int {
        return R.layout.home_work_item
    }


}