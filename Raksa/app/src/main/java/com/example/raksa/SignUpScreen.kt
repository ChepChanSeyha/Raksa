@file:Suppress("PLUGIN_WARNING")

package com.example.raksa

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.raksa.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_driver_sign_up_screen.*

class SignUpScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_sign_up_screen)

        btn_sign_up.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount() {

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = findViewById<RadioButton>(i)

            val fullName = firstName.text.toString() + lastName.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val position = radioButton.text.toString()
            val number = number.text.toString()
            val address = address.text.toString()

            when {
                TextUtils.isEmpty(fullName) -> Toast.makeText(this, "First Name and Last Name are required.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(email) -> Toast.makeText(this, "Email is required.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(password) -> Toast.makeText(this, "Password is required.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(position) -> Toast.makeText(this, "Position is required.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(number) -> Toast.makeText(this, "Number is required.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(address) -> Toast.makeText(this, "Address is required.", Toast.LENGTH_LONG).show()

                else -> {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("SignUp")
                    progressDialog.setMessage("Please wait, this may take a while...")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()

                    val auth = FirebaseAuth.getInstance()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                saveUserInfo(fullName, email, position, number, address)
                            } else {
                                val message = task.exception.toString()
                                Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()
                            }
                        }
                }
            }
        }
    }

    private fun saveUserInfo(fullName: String, email: String, position: String, number: String, address: String, progressDialog: ProgressDialog) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = FirebaseDatabase.getInstance().reference.child("Driver")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = currentUserID
        userMap["email"] = currentUserID
        userMap["position"] = currentUserID
        userMap["number"] = currentUserID
        userMap["address"] = currentUserID
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/raksa-76bec.appspot.com/o/Icons%2Fprofile.png?alt=media&token=dfbabffa-5b49-417c-8c9f-42b3f3561126"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Account has been created successfully.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, Home::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }
    }
}
