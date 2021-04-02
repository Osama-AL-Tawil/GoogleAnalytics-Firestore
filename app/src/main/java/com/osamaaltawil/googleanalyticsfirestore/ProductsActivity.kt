package com.osamaaltawil.googleanalyticsfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_products.*
import java.util.concurrent.TimeUnit

class ProductsActivity : AppCompatActivity() {
    var analytics= FirebaseAnalytics.getInstance(this)
    var db = FirebaseFirestore.getInstance()
    var timeStart:Long=0
    var timeEnd:Long=0
    var data = ArrayList<modle2>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        //Action Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Products"

        //get category name
        val name = intent.getStringExtra("name")

        //store time start------------
        timeStart=System.currentTimeMillis()

        //Google Analytics
        screenView("Products Screen","Products Activity")

        //get data and set view
        if (name != "") {
            db.collection("Categories").get().addOnSuccessListener {querySnapshot->
                //select category products
                for (doc in querySnapshot){
                    val n=doc.getString("ct_name")
                    if (name==n){
                        val doc_id=doc.id
                        //get all products
                        db.collection("Categories").document(doc_id).collection("Products")
                            .get().addOnSuccessListener {
                                for (d in it){
                                    data.add(modle2(d.toObject(mdata::class.java),d.id))

                                }

                                if (data.isNotEmpty()){
                                    rc_setView(Products_Adapter(this, data))
                                    progressBar_pro.visibility= View.GONE

                                }
                            }.addOnFailureListener{
                                Log.e("error",it.message.toString())
                            }
                    }
                }

                }.addOnFailureListener{
                Log.e("error",it.message.toString())
            }

        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun rc_setView(adapter: Products_Adapter){
        rc_pro.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rc_pro.adapter = adapter
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
        val time_spend= TimeUnit.MILLISECONDS.toSeconds(timeEnd-timeStart)
        //add data in hashMap
        val data=HashMap<String,Any>()
        data["Id"]="test id ApJhMOu747LK725"
        data["Screen_Name"]="Products Screen"
        data["Time_Spend"]="$time_spend seconds"

        db.collection("TimeSpend").add(data).addOnFailureListener{ Log.e("time Spend",it.message.toString())}
        super.onDestroy()
    }

}