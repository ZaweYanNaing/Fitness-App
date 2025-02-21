package com.example.assignment_level5

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView



class ProfileFragment : Fragment() {

    private lateinit var editTextIcon: ImageView
    private lateinit var userImage: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var weight: TextView
    private lateinit var phone: TextView
    private lateinit var height: TextView
    private lateinit var gender: TextView
    private lateinit var age: TextView
    private lateinit var location: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize editText_icon
        editTextIcon = view.findViewById(R.id.editProfileinProfile)

        // Initialize user Image
        userImage= view.findViewById(R.id.EdtProfile)

        userImage.setImageResource(
            if (User.getugender()) R.drawable.manlogo else R.drawable.woman
        )



        // Set OnClickListener
        editTextIcon.setOnClickListener {
            startActivity(Intent(requireContext(),EditProfileActivity::class.java))
        }

        // Initialize Personal Data

        name=view.findViewById(R.id.NameinProfile)
        email=view.findViewById(R.id.emailinProfile)
        weight=view.findViewById(R.id.weightinProfile)


        name.text=User.getuname()
        email.text=User.getuemail()
        weight.text=String.format(User.getuweight().toString())



        phone=view.findViewById(R.id.phoneinProfile)
        height=view.findViewById(R.id.heightinProfile)
        gender=view.findViewById(R.id.genderinProfile)
        age=view.findViewById(R.id.ageinProfile)
        location=view.findViewById(R.id.locationinProfile)




        if(User.getuage()>0)
        {
            phone.text=User.getuphone()
            height.text=String.format(User.getuheight().toString())
            age.text=String.format(User.getuage().toString())
            location.text=User.getulocation()

            gender.text = if (User.getugender()) {
                "Male"
            } else {
                "Female".also { gender.text = it }
            }


        }


        else
        {

            val defaultText = "-----"
            val views = listOf(phone, height, gender, age, location)

            views.forEach { v ->
                v.text = defaultText
            }



        }

    }

}