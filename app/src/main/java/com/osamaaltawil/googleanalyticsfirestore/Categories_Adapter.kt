package com.osamaaltawil.googleanalyticsfirestore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.rc_categories.view.*
import kotlinx.android.synthetic.main.rc_products.view.*

class Categories_Adapter (var activity: Activity, var data: ArrayList<modle1>) :
    RecyclerView.Adapter<Categories_Adapter.MyViewHolder>() {

    var analytics= FirebaseAnalytics.getInstance(activity)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.categories_name
        val click = itemView.Frame_layout
        val img = itemView.ct_img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(activity).inflate(R.layout.rc_categories, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //data
        val name=data[position].ct_name
        val img_url=data[position].img_url

        //set data
        holder.name.setText(name)
        Glide.with(activity).load(img_url).into(holder.img)

        //onClick And send data to details Activity
        holder.click.setOnClickListener {
            //Google Analytics
            selectCategory(data[position].ct_name)

            val i= Intent(activity,ProductsActivity::class.java)
            i.putExtra("name",name)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //Google Analytics--Select Category
    fun selectCategory(category_name:String){
        val bundle= Bundle()
        bundle.putString("CategoryName",category_name)
        analytics.logEvent("SelectCategory",bundle)


    }

}
