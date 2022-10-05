package com.twostrangerteam.ijournal.classes

import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_cabinet.view.*

class CabinetItem (val cab: CabinetModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_num_cab.text = cab.num_cab
        viewHolder.itemView.tv_lastname.text = cab.lastname
    }

    override fun getLayout(): Int {
        return R.layout.item_cabinet
    }
}