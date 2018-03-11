package com.example.android.snapchatclone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.example.android.snapchatclone.R.id.imageView
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class AddSnapActivity : AppCompatActivity() {
    var snapImage:ImageView?=null
    var messageText:EditText? = null
    val ImageName = UUID.randomUUID().toString() + ".jpg"
    var image:ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_snap)
        image = findViewById(R.id.imageView)
        messageText = findViewById(R.id.editText3)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        else
            getPhoto()
    }
    fun getPhoto(){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,1)
    }
    fun choose(view: View){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1)
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getPhoto()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var selectedImage = data!!.data
        if(requestCode == 1&&resultCode == Activity.RESULT_OK&&data !=null)
        {
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
                image?.setImageBitmap(bitmap)
            }catch (e:Exception){e.printStackTrace()}
        }
    }
    fun send(view:View){
        // Get the data from an ImageView as bytes
        image?.setDrawingCacheEnabled(true)
        image?.buildDrawingCache()
        val bitmap = image?.getDrawingCache()
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask =FirebaseStorage.getInstance().getReference().child("images").child(ImageName).putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"Upload Failed!",Toast.LENGTH_SHORT).show()
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            val downloadUrl = taskSnapshot.downloadUrl
            var intent = Intent(this,ChooseUserActivity::class.java)
            intent.putExtra("imageURL",downloadUrl.toString())
            intent.putExtra("imageName",ImageName)
            intent.putExtra("message",messageText?.text.toString())
            Log.i("Message ",messageText?.text.toString())
            Log.i("URL",downloadUrl.toString())
            startActivity(intent)

        })

    }
}
