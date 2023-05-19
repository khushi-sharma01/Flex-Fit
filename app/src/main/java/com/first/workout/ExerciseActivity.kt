package com.first.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.first.workout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(binding?.toolBarExercise)

         if(supportActionBar !=null){
             supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }
        }
}