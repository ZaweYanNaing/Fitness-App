package com.example.assignment_level5

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var profilelogo: ImageView
    private lateinit var uname: TextView
    private lateinit var logoutbtn: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_food.xml)
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        // Setup top navigation elements
        topNav(view)

        // Find the map fragment from the child fragment manager and set the callback
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    // This method is called when the map is ready to be used
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Example: add a marker in Mandalay and move the camera
        val mandalayLocation = LatLng(21.9404, 96.1050)
        googleMap.addMarker(
            MarkerOptions()
                .position(mandalayLocation)
                .title("Marker in Mandalay")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mandalayLocation, 10f))
    }

    private fun topNav(view: View) {
        // Initialize views
        profilelogo = view.findViewById(R.id.userlogo)
        uname = view.findViewById(R.id.userName)
        logoutbtn = view.findViewById(R.id.iconLogout)
        bottomNavigationView = activity?.findViewById(R.id.bottom_nav)
            ?: throw IllegalStateException("BottomNavigationView not found")

        // Set up the views (update these methods as per your User model)
        uname.text = User.getuname()
        profilelogo.setImageResource(
            if (User.getugender()) R.drawable.manlogo else R.drawable.woman
        )

        profilelogo.setOnClickListener {
            // Change fragment to ProfileFragment
            User.changeFragment(ProfileFragment(), parentFragmentManager, R.id.fragmentContainerView)
            // Select the 'bottom_user' item in the BottomNavigationView
            bottomNavigationView.selectedItemId = R.id.bottom_user
        }

        logoutbtn.setOnClickListener {
            // Handle logout and navigate back to MainActivity
            User.setuid(-1)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}
