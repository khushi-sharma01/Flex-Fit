package com.first.workout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast

import com.first.workout.databinding.ActivityExerciseBinding
import com.first.workout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
  
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null

    private var player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tts = TextToSpeech(this, this,)

        binding?.toolBarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()

        }
        exerciseList = Constants.defaultExerciseList()
        setupRestView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        //Todo: create a binding variable
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        //Todo: bind to the dialog
        customDialog.setContentView(dialogBinding.root)
        //Todo: to ensure that the user clicks one of the button and that the dialog is
        //not dismissed when surrounding parts of the screen is clicked
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            //Todo We need to specify that we are finishing this activity if not the player
            // continues beeping even after the screen is not visibile
            this@ExerciseActivity.finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }


    private fun setupRestView(){


        try {
            val soundURI = Uri.parse("android.resource://com.first.workout/"+R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping= false
            player?.start()

        }
        catch (e:Exception){
            e.printStackTrace()
        }
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

        speakOut("Take a Rest of 10 sec")
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


        speakOut(exerciseList!![currentExercisePosition].getname())

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
                finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
            }}
        }.start()
    }
  public  override fun onDestroy() {

        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }
      if(tts!=null){
          tts!!.shutdown()
          tts!!.stop()
      }

      if(player!=null){
          player!!.stop()
      }

        binding=null
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if(status== TextToSpeech.SUCCESS){
            val result=tts?.setLanguage(Locale.US)

            if(result== TextToSpeech.LANG_MISSING_DATA|| result== TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","ThisLanguage soecified is not supported")}
        }
       else{
           Log.e("TTS","Initialisation failed!")
       }
    }
    private fun speakOut(text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

}

