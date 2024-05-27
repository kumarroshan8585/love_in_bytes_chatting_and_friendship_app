package com.example.datingapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user= FirebaseAuth.getInstance().currentUser //Checking if the user is authenticated or not?

        Handler(Looper.getMainLooper()).postDelayed({

            if(user==null){
                startActivity(Intent(this, LoginActivity::class.java))  //Agar user authenticated nahi hai to login activity ke paas lekar jao
            }
            else{
                startActivity(Intent(this, MainActivity::class.java))  //Agar authenticated hai to main activity

            }
            finish()
        }, 2000)  //2 seconds tak splash screen rahegi
    }
}