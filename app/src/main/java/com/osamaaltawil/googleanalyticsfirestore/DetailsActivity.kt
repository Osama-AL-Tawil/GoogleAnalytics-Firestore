package com.osamaaltawil.googleanalyticsfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_details.*
import java.util.concurrent.TimeUnit

class DetailsActivity : AppCompatActivity() {
    var db=FirebaseFirestore.getInstance()
    var analytics= FirebaseAnalytics.getInstance(this)
    var timeStart:Long=0
    var timeEnd:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //Action Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Product Details"

        //get data.....
        val name=intent.getStringExtra("name")
        val price=intent.getStringExtra("price")
        val details=intent.getStringExtra("details")
        val img_url=intent.getStringExtra("img_url")
         //store time start------------
        timeStart=System.currentTimeMillis()
        //Google Analytics
        screenView("Details Screen","Details Activity")

        if(name!=""&&details!=""&&img_url!=""&&price!=null){
            tv_details.setText("Details :"+details)
            tv_name.setText("Product Name :"+name)
            tv_price.setText("Price :$"+price)
           Glide.with(this).load(img_url).into(imageView)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //Google Analytics--ScreenView
    fun screenView(ScreenName:String,ScreenClass:String){
        val bundle= Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,ScreenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS,ScreenClass)
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle)


    }

    override fun onDestroy() {
        timeEnd=System.currentTimeMillis()
        val time_spend=TimeUnit.MILLISECONDS.toSeconds(timeEnd-timeStart)
        //add data in hashMap
        val data=HashMap<String,Any>()
        data["user_Id"]="test id ApJhMOu747LK725"
        data["Screen_Name"]="Details Screen"
        data["Time_Spend"]="$time_spend seconds"

        db.collection("TimeSpend").add(data).addOnFailureListener{ Log.e("time Spend",it.message.toString())}
        super.onDestroy()
    }
}