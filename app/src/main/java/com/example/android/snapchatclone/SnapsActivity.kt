package com.example.android.snapchatclone

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.solver.widgets.Snapshot
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.example.android.snapchatclone.R.mipmap.ic_launcher




class SnapsActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    var listview:ListView? = null
    var snaps:ArrayList<String> = ArrayList()
    var adapter:ArrayAdapter<String>? = null
    var snapshot:ArrayList<DataSnapshot>? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var influater = menuInflater
        influater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.add_snap){
            var intent = Intent(this,AddSnapActivity::class.java)
            startActivity(intent)
        }
        else if(item?.itemId == R.id.logout){
            mAuth.signOut()
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        listview = findViewById(R.id.snaps)
        snaps = ArrayList<String>()
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,snaps)
        listview?.adapter = adapter
        snapshot = ArrayList<DataSnapshot>()
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().currentUser?.uid).child("snaps").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                snaps.add(p0?.child("from")?.value as String)
                snapshot?.add(p0)
                adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                var index =0
                for(s:DataSnapshot in snapshot!!){
                    if(s.key == p0?.key)
                    {
                        snapshot?.removeAt(index)
                        snaps?.removeAt(index)
                    }
                    index++
                }
                adapter?.notifyDataSetChanged()
            }

        })
        listview?.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val shot = snapshot?.get(p2)
                var intent = Intent(applicationContext,ViewSnapActivity::class.java)
                intent.putExtra("imageName",shot?.child("imageName")?.value as String)
                intent.putExtra("imageURL",shot?.child("imageURL")?.value as String)
                intent.putExtra("message",shot?.child("message")?.value as String)
                intent.putExtra("snapKey",shot?.key)
                startActivity(intent)
            }

        })

    }
}
