package com.example.assignment_level5

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.time.LocalDate

class ExerciseActivity : AppCompatActivity() {
    private val URL="http://10.0.2.2:8080/addData.php"
    private lateinit var tvTitle:TextView
    private lateinit var caloriesShow:TextView
    private lateinit var timeShow:TextView
    private lateinit var stepsShow:TextView
    private lateinit var imgPhoto:ImageView
    private lateinit var buttonPlay1:ImageView
    private lateinit var buttonPlay2:ImageView
    private lateinit var buttonPause:ImageView
    private lateinit var buttonStop:ImageView
    private lateinit var title:String
    private var actID:Int=0
    private var mET: Float=0.0f
    private var calories:Int=0
    private var step:Int=0
    private var isRunning = false
    private var timerSecond = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timerSecond++
            val hour = String.format("%02d", timerSecond / 3600)
            val minute = String.format("%02d", (timerSecond % 3600) / 60)
            val second = String.format("%02d", timerSecond % 60)

            timeShow.text="$hour:$minute:$second"
            calories=((timerSecond/3600.0)*mET*User.getuweight()).toInt()
            caloriesShow.text=calories.toString()

            if(actID==1)
            {
                step=(timerSecond*1.89f).toInt()
            }

            stepsShow.text=step.toString()




            handler.postDelayed(this, 1000)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise)

        timeShow=findViewById(R.id.TimeData)
        caloriesShow=findViewById(R.id.caloriesData)
        stepsShow=findViewById(R.id.stepsData)

        tvTitle=findViewById(R.id.ExerciseTitle)
        imgPhoto=findViewById(R.id.photoInExercise)


        title = intent.getStringExtra("Name") ?: ""
        actID = intent.getIntExtra("actId", 0)
        mET   = intent.getFloatExtra("met", 0.0f)


        showName()

        clickButton()

    }



    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    private fun clickButton(){

        buttonPlay1=findViewById(R.id.playButton1)
        buttonPlay2=findViewById(R.id.playButton2)
        buttonStop=findViewById(R.id.stopButton)
        buttonPause=findViewById(R.id.pauseButton)


        buttonPlay1.setOnClickListener()
        {
            buttonStop.visibility= View.VISIBLE
            buttonPause.visibility= View.VISIBLE
            buttonPlay1.visibility=View.GONE

            startTimer()
        }

        buttonPause.setOnClickListener()
        {
            buttonPause.visibility=View.GONE
            buttonPlay2.visibility=View.VISIBLE
            stopTimer()

        }

        buttonPlay2.setOnClickListener()
        {
            buttonPlay2.visibility=View.GONE
            buttonPause.visibility=View.VISIBLE
            startTimer()
        }

        buttonStop.setOnClickListener()
        {
            stopTimer()
            if(timerSecond<60)
            {
                finish()
            }
            else{
                val currentDate = LocalDate.now()
                val uid=User.getuid()

                // store exercise date in database
                addExerciseData(uid,actID,currentDate.toString(),calories,step,timerSecond)
                finish()
            }

        }
    }

    private fun showName(){
        // set Title of Exercise
        tvTitle.text=title
        val resID = when (title) {
            "Walking" -> R.drawable.walking_photo
            "Yoga" -> R.drawable.yoga_photo
            "Weight_lift" -> R.drawable.lift_photo
            "Skipping" -> R.drawable.skipping_photo
            "Push_up" -> R.drawable.push_up_photo
            "Boxing" -> R.drawable.boxing_photo
            else -> R.drawable.walking_photo  // Use a default image if the name doesn't match
        }
        // set photo of Exercise
        imgPhoto.setBackgroundResource(resID)
    }

    private fun addExerciseData(uid: Int, aid: Int, amdate: String, calories: Int, step: Int, times: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            { response ->
                try {
                    Log.d("ServerResponse", response) // Log the response for debugging
                    val arr = JSONArray(response)
                    val obj = arr.getJSONObject(0)
                    val status = obj.getInt("status")

                    if (status > 0) {
                        Toast.makeText(this, "Exercise data added successfully", Toast.LENGTH_LONG).show()
                    } else {
                        when (status) {
                            -1 -> Toast.makeText(this, "Query failed", Toast.LENGTH_LONG).show()
                            -2 -> Toast.makeText(this, "Database connection error", Toast.LENGTH_LONG).show()
                            -3 -> Toast.makeText(this, "Missing POST data", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this, "Unknown error: $status", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: JSONException) {
                    Log.e("JSONError", "Failed to parse JSON: ${e.message}")
                    Toast.makeText(this, "Failed to parse server response", Toast.LENGTH_LONG).show()
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
                params["aid"] = aid.toString()
                params["amdate"] = amdate
                params["calories"] = calories.toString()
                params["step"] = step.toString()
                params["times"] = times.toString()
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

}