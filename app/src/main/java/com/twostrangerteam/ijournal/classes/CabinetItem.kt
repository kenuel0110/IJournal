package com.twostrangerteam.ijournal.classes

import android.view.animation.AnimationUtils
import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_cabinet.view.*

class CabinetItem (val cab: CabinetModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.rv_cabinets_cardview.animation = AnimationUtils.loadAnimation(viewHolder.itemView.rv_cabinets_cardview.context, R.anim.item_alpha)


        viewHolder.itemView.tv_num_cab.text = cab.num_cab
        viewHolder.itemView.tv_name.text = cab.lastname
    }

    override fun getLayout(): Int {
        return R.layout.item_cabinet
    }
}