package com.example.assignment_level5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exerciseList: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val aname: TextView = view.findViewById(R.id.aname)
        val calories: TextView = view.findViewById(R.id.caloriesDataInRecyclerView)
        val times: TextView = view.findViewById(R.id.timesDataInRecyclerView)
        val image: ImageView = view.findViewById(R.id.activityImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]
        val timeMin=exercise.times/60

        holder.aname.text = exercise.aname
        holder.calories.text = String.format(exercise.calories.toString())
        holder.times.text = String.format(timeMin.toString())

        val exerciseImageMap = mapOf(
            1 to R.drawable.walk_exercise,
            2 to R.drawable.yoga_exercise,
            3 to R.drawable.lift_exercise,
            4 to R.drawable.skipping_exercise,
            5 to R.drawable.pushup_exercise,
            6 to R.drawable.boxing_exercise
        )

        holder.image.setImageResource(exerciseImageMap[exercise.aid]?: R.drawable.walk_exercise)



    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
