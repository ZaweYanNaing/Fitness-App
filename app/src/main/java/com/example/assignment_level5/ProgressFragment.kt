package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate

class ProgressFragment : Fragment() {

    private val URL_GOALS = "http://10.0.2.2:8080/showgoal.php"
    private val URL_EXERCISE = "http://10.0.2.2:8080/showexercise.php"
    private var totalCalories = 0
    private var totalTime = 0
    private var totalSteps = 0
    private lateinit var tvTotalCalories: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var tvTotalSteps: TextView
    private lateinit var profileLogo: ImageView
    private lateinit var uname: TextView
    private lateinit var logoutBtn: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tvCalories: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvSteps: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val exerciseList = mutableListOf<Exercise>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        tvTotalCalories = view.findViewById(R.id.TotalCalories)
        tvTotalTime = view.findViewById(R.id.TotalTimes)
        tvTotalSteps = view.findViewById(R.id.TotalSteps)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        exerciseAdapter = ExerciseAdapter(exerciseList)
        recyclerView.adapter = exerciseAdapter

        topNav(view)
        val currentDate = LocalDate.now()
        retrieveLastGoal(User.getuid(), currentDate.toString(), view)
        retrieveExerciseData(User.getuid())
        showGoalData(view)

        return view
    }

    private fun showExerciseData() {
        val totalMin = totalTime / 60
        tvTotalCalories.text = String.format(totalCalories.toString())
        tvTotalSteps.text = String.format(totalSteps.toString())
        tvTotalTime.text = String.format(totalMin.toString())
    }

    private fun topNav(view: View) {
        profileLogo = view.findViewById(R.id.userlogo)
        uname = view.findViewById(R.id.userName)
        logoutBtn = view.findViewById(R.id.iconLogout)
        bottomNavigationView = activity?.findViewById(R.id.bottom_nav)
            ?: throw IllegalStateException("BottomNavigationView not found")

        uname.text = User.getuname()
        profileLogo.setImageResource(if (User.getugender()) R.drawable.manlogo else R.drawable.woman)

        profileLogo.setOnClickListener {
            User.changeFragment(ProfileFragment(), parentFragmentManager, R.id.fragmentContainerView)
            bottomNavigationView.selectedItemId = R.id.bottom_user
        }

        logoutBtn.setOnClickListener {
            User.setuid(-1)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun showGoalData(view: View) {
        tvCalories = view.findViewById(R.id.Goal_calories)
        tvTime = view.findViewById(R.id.Goal_time)
        tvSteps = view.findViewById(R.id.Goal_steps)

        tvCalories.text = Goals.getCalories()
        tvTime.text = Goals.getTime()
        tvSteps.text = Goals.getSteps()
    }

    private fun retrieveLastGoal(uid: Int, gdate: String, view: View) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL_GOALS,
            StringRequest@{ response ->
                try {
                    Log.d("ServerResponse", response) // Log the response for debugging

                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.has("status")) {
                            val status = jsonObject.getInt("status")
                            if (status == -1) {
                                Toast.makeText(requireContext(), "No goals found", Toast.LENGTH_LONG).show()
                                return@StringRequest
                            }
                        }
                    } catch (e: JSONException) {
                        // Not a JSON object, proceed to parse as JSONArray
                    }

                    val arr = JSONArray(response)
                    if (arr.length() > 0) {
                        val obj = arr.getJSONObject(0)
                        val calories = obj.getInt("calories")
                        val time = obj.getInt("time")
                        val steps = obj.getInt("steps")

                        Goals.setTime(time.toString())
                        Goals.setSteps(steps.toString())
                        Goals.setCalories(calories.toString())
                        showGoalData(view)
                    }
                } catch (e: JSONException) {
                    Log.e("JSONError", "Failed to parse JSON: ${e.message}")
                    Toast.makeText(requireContext(), "Failed to parse goal data", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("NetworkError", "Error: ${error.message}")
                Toast.makeText(requireContext(), "Network error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = uid.toString()
                params["gdate"] = gdate
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    private fun retrieveExerciseData(uid: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL_EXERCISE,
            { response ->
                try {
                    Log.d("ServerResponse", response) // Log the response for debugging
                    val arr = JSONArray(response)
                    exerciseList.clear()

                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        val aid = obj.getInt("aid")
                        val calories = obj.getInt("calories")
                        val step = obj.getInt("step")
                        val times = obj.getInt("times")
                        val aname = obj.getString("aname")

                        totalCalories += calories
                        totalSteps += step
                        totalTime += times

                        val exercise = Exercise(aid, calories, step, times, aname)
                        exerciseList.add(exercise)
                    }
                    exerciseAdapter.notifyDataSetChanged()
                    showExerciseData()
                } catch (e: JSONException) {
                    Log.e("JSONError", "Failed to parse JSON: ${e.message}")
                    Toast.makeText(context, "Failed to parse exercise data", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("NetworkError", "Error: ${error.message}")
                Toast.makeText(context, "Network error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = uid.toString()
                return params
            }
        }
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
