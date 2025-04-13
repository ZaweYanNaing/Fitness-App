package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class SignInActivity : AppCompatActivity() {
    private val URL = "http://10.0.2.2:8080/login.php"
    private lateinit var btnSignIn: Button
    private lateinit var etemail: EditText
    private lateinit var etpass: EditText
    private lateinit var error: TextView
    private lateinit var hasAccount: TextView
    private lateinit var email: String
    private lateinit var pass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        btnSignIn = findViewById(R.id.btnSignIn)
        error = findViewById(R.id.errorSignIn)
        hasAccount = findViewById(R.id.hasaccount)

        // Enter SignUp Activity
        hasAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        // SignIn start
        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun login() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            { response ->

                try {
                    val arr = JSONArray(response)
                    val obj = arr.getJSONObject(0)
                    val uid = obj.getInt("uid")
                    val uname = obj.getString("uname")
                    val uweight = obj.optString("uweight", "0").toFloatOrNull() ?: 0f
                    val phone = obj.optString("uphone", "")
                    val height = obj.optString("uheight", "0").toFloatOrNull() ?: 0f
                    val gender = obj.getString("ugender")
                    val age = obj.optInt("uage", 0)
                    val location = obj.optString("ulocation", "")
                    val uemail = obj.getString("uemail")

                    if (uid < 0) {
                        Toast.makeText(this, "error $uid", Toast.LENGTH_LONG).show()
                    } else {
                        User.setuid(uid)
                        User.setuname(uname)
                        User.setuweight(uweight)
                        User.setuemail(uemail)

                        if (phone.isEmpty() )
                        {
                            // nothing done
                        }
                        else {
                            User.setuphone(phone)
                            User.setuheight(height)
                            User.setuage(age)
                            User.setulocation(location)
                            if (gender.equals("1")) {
                                User.setugender(true);
                            } else {
                                User.setugender(false);
                            }

                        }

                        Toast.makeText(this, "Successfully Login", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            { error ->
                // Handle error
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val param = HashMap<String, String>()
                param["uemail"] = email
                param["upass"] = pass
                return param
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun signIn()
    {
        etemail = findViewById(R.id.etEmailSignIn)
        etpass = findViewById(R.id.etPassSignIn)
        email = etemail.text.toString()
        pass = etpass.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            login()
        } else {
            Toast.makeText(this, "Enter your Data!", Toast.LENGTH_LONG).show()
        }
    }
}







