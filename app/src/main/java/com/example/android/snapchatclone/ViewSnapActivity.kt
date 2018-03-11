package com.example.android.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ViewSnapActivity : AppCompatActivity() {
    inner class DownLoader : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            var httpURLConnection: HttpURLConnection? = null
            val url: URL
            try {
                url = URL(urls[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connect()
                val inputStream = httpURLConnection.inputStream
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }
    var snapPhoto:ImageView? =null
    var message:TextView?= null
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        snapPhoto = findViewById(R.id.snapphoto)
        message = findViewById(R.id.textView)
        message?.setText(intent.getStringExtra("message"))
        val Snap:Bitmap
        val task:DownLoader = DownLoader()
        Snap = task.execute(intent.getStringExtra("imageURL")).get()
        snapPhoto?.setImageBitmap(Snap)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid).child("snaps")
                .child(intent.getStringExtra("snapKey")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()

    }
}
