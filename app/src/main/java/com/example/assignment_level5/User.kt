package com.example.assignment_level5

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class User {
    companion object {
        private var uid: Int = -1
        private var uname: String = "User"
        private var uemail: String=""
        private var uweight: Float = -1f
        private var ugender: Boolean = true
        private var uphone: String = ""
        private var uheight: Float = -1f
        private var uage: Int = -2
        private var ulocation: String = ""


        fun getuemail(): String {
            return this.uemail
        }

        fun setuemail(e: String) {
            this.uemail=e
        }

        fun getuphone(): String {
            return this.uphone
        }

        fun setuphone(ph: String) {
            this.uphone = ph
        }

        fun getuheight(): Float {
            return this.uheight
        }

        fun setuheight(h: Float) {
            this.uheight = h
        }

        fun getuage(): Int {
            return this.uage
        }

        fun setuage(a: Int) {
            this.uage = a
        }

        fun getulocation(): String {
            return this.ulocation
        }

        fun setulocation(l: String) {
            this.ulocation=l
        }

        fun getuweight(): Float {
            return this.uweight
        }

        fun setuweight(w: Float) {
            this.uweight=w
        }

        fun getugender(): Boolean
        {
            return this.ugender
        }

        fun setugender(g: Boolean)
        {
            this.ugender=g
        }

        fun getuid(): Int {
            return uid
        }

        fun setuid(id: Int) {
            uid = id
        }

        fun getuname(): String
        {
            return this.uname
        }

        fun setuname(name: String)
        {
            this.uname=name
        }

        fun changeFragment(fragment: Fragment, fragmentManager: FragmentManager, id:Int)
        {

            fragmentManager.beginTransaction().replace(id,fragment).commit()
        }


    }
}