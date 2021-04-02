package com.osamaaltawil.googleanalyticsfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    var db=FirebaseFirestore.getInstance()
    var analytics= FirebaseAnalytics.getInstance(this)
    var timeStart:Long=0
    var timeEnd:Long=0
    var data=ArrayList<modle1>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title="Categories"

        //store time start------------
        timeStart=System.currentTimeMillis()

        //Google Analytics
        screenView("Categories Screen","Main Activity")
        //get data
        db.collection("Categories").get().addOnSuccessListener {
                data.addAll(it.toObjects(modle1::class.java))
                if (data.isNotEmpty()){
                    rc_setView(Categories_Adapter(this, data))
                    progressBar_ct.visibility= View.GONE

                }
            }
    }
    fun rc_setView(adapter: Categories_Adapter){
        rc_ct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rc_ct.adapter = adapter
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
        data["Screen_Name"]="Categories Screen"
        data["Time_Spend"]="$time_spend seconds"

        db.collection("TimeSpend").add(data).addOnFailureListener{ Log.e("time Spend",it.message.toString())}
        super.onDestroy()
    }
}
