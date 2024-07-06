package com.example.datingapp.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityRegistrationBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.ui.VideoCall
import com.example.datingapp.utils.Config
import com.example.datingapp.utils.Config.hideDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    private var imageUri: Uri?=null  //taking the image of user

    private val selectImage=registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it

        binding.userImage.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.saveData.setOnClickListener {
            validateData()
        }
    }

//    private fun validateData() {
//        if(binding.userName.text.toString().isEmpty() ||
//            binding.userCity.text.toString().isEmpty()
//            || binding.userEmail.text.toString().isEmpty() ||
//            imageUri==null
//            ){
//                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
//        }else if(!binding.termsCondition.isChecked){
//            Toast.makeText(this, "Please accept terms and conditions.", Toast.LENGTH_SHORT).show()
//        }else{
//            uploadImage()
//        }
//    }

//    private fun validateData() {
//        if(binding.userName.text.toString().isEmpty() ||
//            binding.userCity.text.toString().isEmpty()
//            || binding.userEmail.text.toString().isEmpty()
//            || imageUri == null
//        ){
//            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
//        } else if (!binding.termsCondition.isChecked) {
//            Toast.makeText(this, "Please accept terms and conditions.", Toast.LENGTH_SHORT).show()
//        } else {
//            uploadImage()
//
//            // Start VideoCall activity with extras
//            val intent = Intent(this, VideoCall::class.java).also {
//                it.putExtra("NUMBER", binding.userCity.text.toString())
//                it.putExtra("NAME", binding.userName.text.toString())
//                startActivity(it)
//            }
//
//        }
//    }
//
//
//
//    private fun uploadImage() {
//    Config.showDialog(this)    //Indicating to the user that an upload process is starting.
//
//    val storageRef = FirebaseStorage.getInstance().getReference("profile")   //This line gets a reference to a location in Firebase Storage where the profile image will be uploaded.
//        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")  //The location is specific to the current user, using their unique user ID (uid).
//
//
//    //This block uploads the image file located at imageUri to the specified Firebase Storage reference:
//    storageRef.putFile(imageUri!!)
//        .addOnSuccessListener {  //This block uploads the image file located at imageUri to the specified Firebase Storage reference:
//            storageRef.downloadUrl.addOnSuccessListener { uri ->
//                storeData(uri)
//            }.addOnFailureListener { exception ->   //On Failure: If either the upload or the retrieval of the download URL fails, it hides the dialog and shows a toast message with the error.
//                hideDialog()
//                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//        .addOnFailureListener { exception ->
//            hideDialog()
//            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//        }
//}
//
//
////This line creates an instance of UserModel with the user's name, email, city, and the image URL.
//    private fun storeData(imageUrl: Uri?) {
//    val currentUser = FirebaseAuth.getInstance().currentUser
//    val phoneNumber=currentUser?.phoneNumber
//        val data= UserModel(
//            name=binding.userName.text.toString(),
//            image=imageUrl.toString(),
//            email = binding.userEmail.text.toString(),
//            city=binding.userCity.text.toString(),
//            number = phoneNumber ?: ""
//        )
//
//        FirebaseDatabase.getInstance().getReference("users")  //Gets a reference to the users node in the Firebase Realtime Database.
//            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)  //Uses the user's phone number as the key to store their data.
//            .setValue(data).addOnCompleteListener{  //Stores the UserModel instance at this location.
//
////On Completion: Hides the dialog and, if the operation is successful, starts MainActivity and finishes the current activity.
//                // If there's an error, it shows a toast message with the error.
//                hideDialog()
//                if(it.isSuccessful){
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
////                    Toast.makeText(this, "User register successful", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText( this, it.exception!!.message,Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private var name: String = ""
    private var number: String = ""
    private fun validateData() {
        if (binding.userName.text.toString().isEmpty() ||
            binding.userCity.text.toString().isEmpty() ||
            binding.userEmail.text.toString().isEmpty() ||
            imageUri == null
        ) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        } else if (!binding.termsCondition.isChecked) {
            Toast.makeText(this, "Please accept terms and conditions.", Toast.LENGTH_SHORT).show()
        } else {
            // Save name and number globally
            name = binding.userName.text.toString()
            number = binding.userCity.text.toString()

            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    storeData(uri)
                }.addOnFailureListener { exception ->
                    hideDialog()
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                hideDialog()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val phoneNumber = currentUser?.phoneNumber

        val data = UserModel(
            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.userEmail.text.toString(),
            city = binding.userCity.text.toString(),
            number = phoneNumber ?: ""
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data)
            .addOnCompleteListener {
                hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

        // Start VideoCall activity with extras
        val intent = Intent(this, VideoCall::class.java).apply {
            putExtra("NUMBER", binding.userCity.text.toString())
            putExtra("NAME", binding.userName.text.toString())
        }
        startActivity(intent)
    }

    //From line 60-108:
    //The code first uploads a profile image to Firebase Storage and retrieves its download URL.
// Then, it creates a UserModel instance with the user's details and stores this data in Firebase Realtime Database under the user's phone number.
// Throughout the process, dialogs and toast messages are used to indicate progress and handle errors.

    //What is download URL, and how is it used?:
    //The download URL is essential because it allows the application to access the uploaded image from Firebase Storage.
// By obtaining this URL, the app can reference the image in other parts of the app, such as displaying the user's profile picture.
}