package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class SignUpActivity : AppCompatActivity() {
    private val URL="http://10.0.2.2:8080/register.php"
    private lateinit var fManager: FragmentManager
    private lateinit var btnSignUp: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var edtConPass: EditText
    private lateinit var edtWeight: EditText
    private lateinit var error: TextView
    private lateinit var hasAccount: TextView
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var conpass: String
    private lateinit var name: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        var isSignUp=false
        fManager=supportFragmentManager
        error=findViewById(R.id.errorSignUp)
        hasAccount=findViewById(R.id.hasaccount)

        // Enter SignIn Activity
        hasAccount.setOnClickListener()
        {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }


        btnSignUp = findViewById(R.id.btnSignUp)
        User.changeFragment(SignUpFragment1(),fManager,R.id.fragmentContainer)
        btnSignUp.setOnClickListener {
            if(!isSignUp)
            {

                edtEmail=findViewById(R.id.etEmailSignUp)
                edtPass=findViewById(R.id.etPassSignUp)
                edtConPass=findViewById(R.id.etConPassSignUp)

                 email=edtEmail.text.toString()
                 pass=edtPass.text.toString()
                 conpass=edtConPass.text.toString()

                if(email.isNotEmpty() && pass.isNotEmpty() && conpass.isNotEmpty())
                {
                    if(pass.equals(conpass))
                    {
                        name=email.substring(0,4)
                        isSignUp=true
                        User.changeFragment(SignUpFragment2(),fManager,R.id.fragmentContainer)

                    }
                    else
                    {
                        Toast.makeText(this,"Please Enter The Same Password", Toast.LENGTH_LONG).show()

                    }

                }
                else
                {
                    Toast.makeText(this,"Please Enter your Data", Toast.LENGTH_LONG).show()
                }



            }
            else
            {
                edtWeight=findViewById(R.id.edweight)
                if(edtWeight.text.isNotEmpty())
                {
                    val weight=edtWeight.text.toString().toFloat()

                    // Sign Up start
                    if(register(weight))
                    {

                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this,"Register Fail !", Toast.LENGTH_LONG).show()

                    }


                }
                else
                {
                    Toast.makeText(this,"Please Enter your Weight", Toast.LENGTH_LONG).show()
                }

            }
        }
    }




    private fun register(w: Float): Boolean {
        var r=true
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            { response ->
                try {
                    Log.d("ServerResponse", response) // Log the response
                    val jsonArray = JSONArray(response)
                    val obj = jsonArray.getJSONObject(0)
                    val cid = obj.getInt("cid")

                    if (cid < 0) {
                        when (cid) {
                            -1 -> Toast.makeText(this, "Query failed", Toast.LENGTH_LONG).show()
                            -2 -> Toast.makeText(this, "Database connection error", Toast.LENGTH_LONG).show()
                            -3 -> Toast.makeText(this, "Missing POST data", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this, "Unknown error: $cid", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        r=true
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                    }
                } catch (e: JSONException) {
                    Log.e("JSONError", "Failed to parse JSON: ${e.message}")
                    Toast.makeText(this, "Invalid server response", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("NetworkError", "Error: ${error.message}")
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["uname"] = name
                params["uemail"] = email
                params["upass"] = pass
                params["uweight"] = w.toString()
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
        return r
    }


}
