package com.example.android.snapchatclone

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChooseUserActivity : AppCompatActivity() {
    var listview: ListView? = null
    var emails:ArrayList<String>? = null
    var keys:ArrayList<String>? = null

    var adapter:ArrayAdapter<String>? = null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("users")
    var ScreenX = null
    var ScreenY = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)
        listview = findViewById(R.id.listview)
        emails = ArrayList<String>()
        keys = ArrayList<String>()
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        listview?.adapter=adapter
        // Read from the database
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val email = p0?.child("email")?.value as String
                emails?.add(email)
                keys?.add(p0?.key)
                adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
        listview?.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val display = windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = size.x
                val height = size.y
                p1?.animate()?.translationXBy((width-(p0?.x!!)))?.setDuration(3000)
                val map:Map<String,String>? = mapOf("from" to FirebaseAuth.getInstance().currentUser?.email.toString(),"imageName" to intent.getStringExtra("imageName")
                        ,"imageURL" to intent.getStringExtra("imageURL"),"message" to intent.getStringExtra("message"))
                database.getReference().child("users").child(keys?.get(p2)).child("snaps").push().setValue(map)
            }

        })
    }
}
