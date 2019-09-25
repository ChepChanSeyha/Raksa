package com.example.raksa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_driver_sign_in_screen.*

class SignInScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_sign_in_screen)

        text_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
        }
    }
}
