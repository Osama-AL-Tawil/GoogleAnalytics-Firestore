package com.osamaaltawil.googleanalyticsfirestore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.rc_products.view.*

class Products_Adapter (var activity: Activity, var data: ArrayList<modle2>) :
    RecyclerView.Adapter<Products_Adapter.MyViewHolder>() {

    var analytics= FirebaseAnalytics.getInstance(activity)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tv_pro_name
        val price = itemView.tv_pro_price
        val details = itemView.tv_pro_details
        val imgView = itemView.pro_img
        val click = itemView.CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(activity).inflate(R.layout.rc_products, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //data
        val name=data[position].mdata.name
        val details=data[position].mdata.details
        val price=data[position].mdata.price
        val img_url=data[position].mdata.image

        //set data
        holder.name.setText("Product Name: " +name )
        holder.details.setText("Product Details: " + details)
        holder.price.setText("Price: $" + price)
        Glide.with(activity).load(img_url).into(holder.imgView)

        //onClick And send data to details Activity
        holder.click.setOnClickListener {
            //google Analytics
            selectProduct(data[position].documentId,name,img_url)

            //Toast.makeText(activity,data[position].documentId,Toast.LENGTH_LONG).show()
            val i= Intent(activity,DetailsActivity::class.java)
            i.putExtra("name",name)
            i.putExtra("details",details)
            i.putExtra("price",price)
            i.putExtra("img_url",img_url)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //Google Analytics--Select Product
    fun selectProduct(id:String,product_name:String,product_image:String){
        val bundle= Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,product_name)
        bundle.putString("product_image_url",product_image)
        analytics.logEvent("SelectProduct",bundle)


    }
}
