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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException

class HomeFragment : Fragment() {

    private lateinit var profileLogo: ImageView
    private lateinit var uname: TextView
    private lateinit var logoutBtn: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private val exerciseList = mutableListOf<Exercise>()
    private val URL_EXERCISE = "http://10.0.2.2:8080/showexercise.php"
    private var totalCalories = 0
    private var totalTime = 0
    private var totalSteps = 0
    private lateinit var tvTotalCalories: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var tvTotalSteps: TextView

    private val exercises = listOf(
        ExerciseData(R.id.walk, "Walking", 1, 52.5f),
        ExerciseData(R.id.lift, "Weight_lift", 3, 60.99f),
        ExerciseData(R.id.yoga, "Yoga", 2, 40.9f),
        ExerciseData(R.id.skip, "Skipping", 4, 90.3f),
        ExerciseData(R.id.push_up, "Push_up", 5, 69.5f),
        ExerciseData(R.id.boxing, "Boxing", 6, 95.8f)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        retrieveExerciseData(User.getuid())
        tvTotalCalories = view.findViewById(R.id.tvcalories_show)
        tvTotalTime = view.findViewById(R.id.tvtime_show)
        tvTotalSteps = view.findViewById(R.id.tvdistance_show)



        topNav(view)
        setExerciseListeners(view)
        return view
    }

    private fun topNav(view: View) {
        profileLogo = view.findViewById(R.id.userlogo)
        uname = view.findViewById(R.id.userName)
        logoutBtn = view.findViewById(R.id.iconLogout)
        bottomNavigationView = activity?.findViewById(R.id.bottom_nav) ?: throw IllegalStateException("BottomNavigationView not found")

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

    private fun showExerciseData() {
        val totalMin = totalTime / 60
        val totalMile= totalSteps / 2000.0
        tvTotalCalories.text = String.format(totalCalories.toString())
        tvTotalSteps.text = String.format("%.2f",totalMile)
        tvTotalTime.text = String.format(totalMin.toString())
    }

    private fun setExerciseListeners(view: View) {
        exercises.forEach { exercise ->
            view.findViewById<ImageView>(exercise.logoId).setOnClickListener {
                val intent = Intent(requireContext(), ExerciseActivity::class.java).apply {
                    putExtra("Name", exercise.name)
                    putExtra("actId", exercise.actId)
                    putExtra("met", exercise.met)
                }
                startActivity(intent)
            }
        }
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


                    }
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

    data class ExerciseData(val logoId: Int, val name: String, val actId: Int, val met: Float)
}
