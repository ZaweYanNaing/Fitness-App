package com.example.assignment_level5

class Goals {
    companion object {
        private var calories:String="0"
        private var steps: String="0"
        private var time: String="0"


        fun setCalories(c:String)
        {
            this.calories=c
        }

        fun setSteps(s: String)
        {
            this.steps=s
        }

        fun setTime(t:String)
        {
            this.time=t
        }


        fun getCalories(): String
        {
            return this.calories
        }

        fun getSteps(): String
        {
            return this.steps
        }

        fun getTime(): String
        {
            return this.time
        }

    }

}