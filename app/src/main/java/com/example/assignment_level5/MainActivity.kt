package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnSignInL: Button
    private lateinit var btnSignUpL: Button
    private var uid: Int= User.getuid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if(uid<0)
        {
            setContentView(R.layout.activity_main)
            btnSignInL=findViewById(R.id.btnSignInland)
            btnSignUpL=findViewById(R.id.btnSignUpland)
            btnSignInL.setOnClickListener()
            {

                val i= Intent(this,SignInActivity::class.java)
                startActivity(i)
                finish()

            }

            btnSignUpL.setOnClickListener()
            {
                startActivity(Intent(this,SignUpActivity::class.java))
                finish()
            }
        }
        else
        {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

    }
}