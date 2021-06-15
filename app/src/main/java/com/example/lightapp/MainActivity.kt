package com.example.lightapp


import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var lightOnOff : ImageButton
    private lateinit var textOnOff : TextView

    private var hasLight = false
    private var lightOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lightOnOff = findViewById(R.id.ib_lightOnOff)
        textOnOff = findViewById(R.id.tv_light_on_off)
        lightOnOff.setOnClickListener { turnLightOnOff() }
    }

    override fun onStop() {
        super.onStop()
        if (lightOn){
            textOnOff.text = getText(R.string.turn_on_text)
            lightOnOff.setImageResource(R.drawable.ic_highlight)
            changeLight()
        }
    }

    private fun turnLightOnOff() {
        hasLight = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        //verify that camera has flash if not, return after displaying a toast
        if (!hasLight){
            Toast.makeText(this, "No Flash detected", Toast.LENGTH_LONG).show()
            return
        }

        //initially we expect the light to be off so lightOn = false
        if (!lightOn){
            //if the light is OFF
            textOnOff.text = getText(R.string.turn_off_text)
            lightOnOff.setImageResource(R.drawable.ic_highlight_off)
            changeLight()
        } else {
            //if the light is ON turn it OFF
            textOnOff.text = getText(R.string.turn_on_text)
            lightOnOff.setImageResource(R.drawable.ic_highlight)
            changeLight()
        }

    }

    //change light uses a try/catch to change the state of the light,
    //if it fails, it returns the UI to the previous state
    //and displays the error message.
    private fun changeLight(){
        lightOn = !lightOn
        val camManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = camManager.getCameraIdList()[0]
            camManager.setTorchMode(cameraId, lightOn)
        } catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            lightOn = !lightOn
            error()
        }
    }
    private fun error() {
        if(lightOn){
            textOnOff.text = getText(R.string.turn_off_text)
            lightOnOff.setImageResource(R.drawable.ic_highlight_off)
        } else {
            textOnOff.text = getText(R.string.turn_on_text)
            lightOnOff.setImageResource(R.drawable.ic_highlight)
        }
    }
}