package com.first.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast

import com.first.workout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding?= null

    private var restTimer: CountDownTimer?=null
    private var restProgress=0

    private var exerciseTimer: CountDownTimer?=null
    private var exerciseProgress=0

    private var exerciseList : ArrayList <ExerciseModel>?=null
    private var currentExercisePosition= -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

         if(supportActionBar !=null){
             supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        binding?.toolBarExercise?.setNavigationOnClickListener{
          onBackPressed()
        }
        exerciseList= Constants.defaultExerciseList()
        setupRestView()
        }
    private fun setupRestView(){
        binding?.flRestView?.visibility= View.VISIBLE
        binding?.tvTittle?.visibility=View.VISIBLE
        binding?.tvExerciseName?.visibility=View.INVISIBLE
        binding?.ivImage?.visibility= View.INVISIBLE
        binding?.flExerciseView?.visibility= View.INVISIBLE
        binding?.tvupExerciseName?.visibility=View.VISIBLE

        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0

        }
        binding?.tvupExerciseName?.text = exerciseList!![currentExercisePosition +1].getname()

        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text=(10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()

            }
        }.start()
    }


    private fun setupExerciseView(){
       binding?.flRestView?.visibility= View.INVISIBLE
       binding?.tvTittle?.visibility=View.INVISIBLE
       binding?.tvExerciseName?.visibility=View.VISIBLE
       binding?.ivImage?.visibility= View.VISIBLE
       binding?.flExerciseView?.visibility= View.VISIBLE
        binding?.tvupExerciseName?.visibility=View.INVISIBLE

       if(
           exerciseTimer!=null){
           exerciseTimer?.cancel()
           exerciseProgress=0 }

       binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getimage())
       binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getname()
       setExerciseProgressBar()
   }


    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text=(30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                }
                else{
                Toast.makeText(
                    this@ExerciseActivity,
                    "Congratulations ! you have completed the 7 minutes workout.",
                    Toast.LENGTH_SHORT
                ).show()
            }}
        }.start()
    }
  public  override fun onDestroy() {

        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }



        binding=null
        super.onDestroy()
    }

}

