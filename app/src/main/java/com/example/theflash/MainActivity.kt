package com.example.theflash

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.example.theflash.databinding.ActivityMainBinding
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var cameraManager: CameraManager
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        setupAppName()

        binding.apply {
            val hasLight = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if(!hasLight){
                //show error
                light.isEnabled = false
                Toast.makeText(this@MainActivity, "Tafuta simu ikona flash buda", Toast.LENGTH_SHORT).show()
            }
            //listener
            mainLayout.setTransitionListener(object : MotionLayout.TransitionListener{
                override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == motionLayout!!.endState)
                    {
                        try {
                            val cameraId = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(cameraId, true)
                        }catch (e : CameraAccessException){
                            e.printStackTrace()
                        }catch (e : ArrayIndexOutOfBoundsException){
                            e.printStackTrace()
                        }
                    }else{
                        try {
                            val cameraId = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(cameraId, false)
                        }catch (e : CameraAccessException){ e.printStackTrace()
                        }catch (e : ArrayIndexOutOfBoundsException){ e.printStackTrace() }
                    }
                }

                override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}



            })
        }


    }

    private fun setupAppName(){
val spannableString = SpannableString(getString(R.string.topdisplay))
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorGreen))
        spannableString.setSpan(colorSpan, 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.title.text  = spannableString
    }
}