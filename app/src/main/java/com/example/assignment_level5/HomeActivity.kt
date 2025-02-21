package com.example.assignment_level5
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {
    private val URL="http://10.0.2.2:8080/goal.php"
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fManager: FragmentManager
    private lateinit var llsetGoal: LinearLayout
    private lateinit var setGoal_btn:ImageView
    private lateinit var edcalories:EditText
    private lateinit var edtime:EditText
    private lateinit var edsteps:EditText
    private lateinit var calories:String
    private lateinit var time:String
    private lateinit var steps:String
    private lateinit var btnSetGoal: Button
    private lateinit var closeBtn:ImageView




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced=false
        setContentView(R.layout.activity_home)


        //set Goal function
        setGoal()


        // Bottom nav start ----
        bottomNav()


    }


    private fun bottomNav()
    {
        fManager=supportFragmentManager
        bottomNavigationView=findViewById(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId)
            {
                R.id.bottom_home->
                {
                    User.changeFragment(HomeFragment(),fManager,R.id.fragmentContainerView)
                    true
                }

                R.id.bottom_gps->
                {
                    User.changeFragment(LocationFragment(),fManager,R.id.fragmentContainerView)
                    true
                }

                R.id.bottom_progress->
                {
                    User.changeFragment(ProgressFragment(),fManager,R.id.fragmentContainerView)
                    true
                }

                R.id.bottom_user->
                {
                    User.changeFragment(ProfileFragment(),fManager,R.id.fragmentContainerView)
                    true
                }

                else->false
            }

        }
    }

    private fun setGoal()
    {
        llsetGoal=findViewById(R.id.setGoalll)
        setGoal_btn=findViewById(R.id.add_goal)
        btnSetGoal=findViewById(R.id.setGoal_btn)
        closeBtn=findViewById(R.id.CloseButton)


        // input for Goals
        edcalories=findViewById(R.id.edsetCalories)
        edtime=findViewById(R.id.edsetTime)
        edsteps=findViewById(R.id.edsetStep)




        // add icon click
        setGoal_btn.setOnClickListener()
        {
            llsetGoal.visibility= View.VISIBLE
        }


        // close button click
        closeBtn.setOnClickListener()
        {
            llsetGoal.visibility= View.GONE
        }

        // save button click
        btnSetGoal.setOnClickListener()
        {

            calories=edcalories.text.toString()
            time=edtime.text.toString()
            steps=edsteps.text.toString()

            if(calories.isNotEmpty() && time.isNotEmpty() && steps.isNotEmpty())
            {
                val currentDate = LocalDate.now()
                if(setGoal(User.getuid(),currentDate.toString(),calories.toInt(),time.toInt(),steps.toInt()))
                {
                    llsetGoal.visibility= View.GONE

                }
            }
            else
            {
                Toast.makeText(this,"Enter The Goal Data First!",Toast.LENGTH_SHORT).show()

            }
        }


    }

    private fun setGoal(uid: Int, gdate: String, calories: Int, time: Int, steps: Int): Boolean
    {
        var isSuccess = true
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            { response ->
                try {
                    Log.d("ServerResponse", response) // Log the response
                    val jsonArray = JSONArray(response)
                    val obj = jsonArray.getJSONObject(0)
                    val gid = obj.getInt("gid")

                    if (gid < 0) {
                        when (gid) {
                            -1 -> Toast.makeText(this, "Query failed", Toast.LENGTH_LONG).show()
                            -2 -> Toast.makeText(this, "Database connection error", Toast.LENGTH_LONG).show()
                            -3 -> Toast.makeText(this, "Missing POST data", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this, "Unknown error: $gid", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        isSuccess = true
                        Goals.setTime(time.toString())
                        Goals.setCalories(calories.toString())
                        Goals.setSteps(steps.toString())
                        Toast.makeText(this, "Goal Set Successfully", Toast.LENGTH_LONG).show()
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
                params["uid"] = uid.toString()
                params["gdate"] = gdate
                params["calories"] = calories.toString()
                params["time"] = time.toString()
                params["steps"] = steps.toString()
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
        return isSuccess
    }



}