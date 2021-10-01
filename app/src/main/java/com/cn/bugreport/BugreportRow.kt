package com.cn.bugreport

import android.util.Log
import com.cn.bugreport.models.Bugreport
import com.cn.bugreport.models.User
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.bugreport_row.view.*

class BugreportRow(val bugreport: Bugreport): Item<GroupieViewHolder>() {
    var bugreporter: User? = null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val reporterId = bugreport.reporterId
        viewHolder.itemView.title_textview_bugreport_row.text = bugreport.title
        viewHolder.itemView.content_textview_bugreport_row.text = bugreport.content
        Log.i("BugreporterRow", "Got value $bugreport")
        FirebaseDatabase.getInstance().getReference("/users").child(reporterId).get().addOnSuccessListener {
            bugreporter = it.getValue(User::class.java)
            viewHolder.itemView.username_textview_bugreport_row.text = bugreporter?.username
            Picasso.get().load(bugreporter?.profileImageUrl).into(viewHolder.itemView.imageView_bugreport_row)
        }.addOnFailureListener {
            Log.e("BugreporterRow", "Error getting data", it)
        }

        viewHolder.itemView.imageView_bugreport_row
    }

    override fun getLayout(): Int {
        return R.layout.bugreport_row
    }
}