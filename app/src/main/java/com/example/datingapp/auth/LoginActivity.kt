package com.example.datingapp.auth

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.annotations.VisibleForTesting
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
//    lateinit=non-null variable that will be initialized later
    private lateinit var binding : ActivityLoginBinding

    val auth=FirebaseAuth.getInstance()  //performing various authentication-related operations, such as user sign-in, sign-up, password reset, and managing user sessions.
    private var verificationId: String?=null

    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog=AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)  //loading ke bahar click arne par cancel nahi hoga
            .create()

        binding.sendOTP.setOnClickListener {

            if(binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error="Please enter your number"
            }else{
                sendOTP(binding.userNumber.text.toString())
            }
        }


        binding.verifyOTP.setOnClickListener{
            if(binding.userOTP.text!!.isEmpty()){
                binding.userOTP.error="Please enter your OTP"
            }else{
                verifyOTP(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyOTP(otp: String) {
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOTP(number: String) {
        //Callback copied from firebase docs
        dialog.show()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId=verificationId

                dialog.dismiss()

                binding.numberLayout.visibility=GONE
                binding.otpLayout.visibility=VISIBLE
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    checkUserExist(binding.userNumber.text.toString())
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91$number") //Here we are actually checking in the firebase DB, whether the user exists or not
            .addValueEventListener(object : ValueEventListener { //listens for changes to the data at this location in the database.
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {  //Agar ye number se entry hai database mai to vo main activity mai bhej dega
                    if (p0.exists()) {
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
                        finish()

                    }
                }
            })
    }
}