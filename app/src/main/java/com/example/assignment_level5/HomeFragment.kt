package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var profileLogo: ImageView
    private lateinit var uname: TextView
    private lateinit var logoutBtn: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView

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

    data class ExerciseData(val logoId: Int, val name: String, val actId: Int, val met: Float)
}
