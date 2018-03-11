package com.example.android.snapchatclone

import android.support.v7.app.*
import android.os.*
import android.view.*
import android.widget.*
import com.google.firebase.*
import com.google.firebase.auth.*
import android.content.Intent
import java.time.Instant
import com.google.firebase.database.FirebaseDatabase




class MainActivity : AppCompatActivity() {
    fun go(v : View?){
        mAuth.signInWithEmailAndPassword(emailText?.text.toString(), passwordText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        login()
                        Toast.makeText(this,"Logging..",Toast.LENGTH_SHORT).show()
                    } else {
                        mAuth.createUserWithEmailAndPassword(emailText?.text.toString(),passwordText?.text.toString())
                                .addOnCompleteListener(this){ task ->
                                    if(task.isSuccessful) {
                                        Toast.makeText(this, "Signed Up, opening now", Toast.LENGTH_SHORT).show()
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid).child("email").setValue(emailText?.text.toString())

                                    }
                                    else{
                                        Toast.makeText(this,"Failed to login .. try again",Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }
    }
    fun login(){
        var intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)
        finish()

    }
    var emailText :EditText? = null
    var passwordText :EditText? = null
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailText  = findViewById(R.id.editText)
        passwordText =findViewById(R.id.editText2)
        if(mAuth.currentUser != null)
            login()
    }
}
