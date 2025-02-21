package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class EditProfileActivity : AppCompatActivity() {


    private var URL = "http://10.0.2.2:8080/edit.php"

    private lateinit var btnsave: Button
    private lateinit var btnback: ImageView
    private lateinit var edname: EditText
    private lateinit var edphone: EditText
    private lateinit var edemail: EditText
    private lateinit var edweight: EditText
    private lateinit var edheight: EditText
    private lateinit var edage: EditText
    private lateinit var edlocation: EditText

    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var weight: String
    private lateinit var height: String
    private var          gender: Boolean = true
    private lateinit var age: String
    private lateinit var location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        val genderSpinner: Spinner = findViewById(R.id.gender_spinner)


        // Initialize spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            R.layout.spinner_item
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        genderSpinner.adapter = spinnerAdapter

        // Set item selected listener
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ans = parent.getItemAtPosition(position).toString()
                gender = if (ans.equals("Male")) {
                    true
                } else {
                    false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = true
            }
        }
        edname = findViewById(R.id.nameinEditProfile)
        edemail = findViewById(R.id.emailinEditProfile)
        edphone = findViewById(R.id.phoneinEditProfile)
        edage = findViewById(R.id.ageinEditProfile)
        edweight = findViewById(R.id.weightinEditProfile)
        edheight = findViewById(R.id.heightinEditProfile)
        edlocation = findViewById(R.id.locationinEditProfile)


        // Save Button and action
        btnsave = findViewById(R.id.btnSave)
        btnsave.setOnClickListener()
        {

            if (edname.text.isNotEmpty() && edemail.text.isNotEmpty() && edphone.text.isNotEmpty() && edweight.text.isNotEmpty() && edweight.text.isNotEmpty() && edlocation.text.isNotEmpty() && edage.text.isNotEmpty()) {

                name = edname.text.toString()
                email = edemail.text.toString()
                phone = edphone.text.toString()
                weight = edweight.text.toString()
                height = edheight.text.toString()
                location = edlocation.text.toString()
                age = edage.text.toString()


                User.setuname(name)
                User.setuemail(email)
                User.setuphone(phone)
                User.setuweight(weight.toFloat())
                User.setuheight(height.toFloat())
                User.setulocation(location)
                User.setugender(gender)
                User.setuage(age.toInt())



                edit()

            } else {
                Toast.makeText(this, "Enter Your Data Before Save", Toast.LENGTH_LONG).show()
            }
        }


        // back icon
        btnback = findViewById(R.id.backicon_edit)
        btnback.setOnClickListener()
        {
            finish()

        }


    }


    private fun edit() {
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

                            else -> Toast.makeText(this, "Unknown error: $cid", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {

                        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()

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
                params["uphone"] = phone
                params["uemail"] = email
                params["uweight"] = weight
                params["uheight"] = height
                params["ugender"] = gender.toString()
                params["uage"] = age
                params["ulocation"] = location
                params["uid"]= User.getuid().toString()


                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)

    }


}