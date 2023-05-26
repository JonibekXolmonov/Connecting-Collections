package com.cc.connectingcollection.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import com.cc.connectingcollection.service.BackgroundMusicService
import com.cc.connectingcollection.utils.Contants.SOUND
import com.cc.connectingcollection.utils.Contants.VIBRATION
import com.cc.connectingcollection.utils.SharedPref


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val sharedPref = SharedPref(context)

    fun startService() {
        val serviceIntent = Intent(context, BackgroundMusicService::class.java)
        context.startService(serviceIntent)
    }

    fun stopService() {
        val serviceIntent = Intent(context, BackgroundMusicService::class.java)
        context.startService(serviceIntent)
    }

    fun playBackgroundMusic() {
        if (sharedPref.getBoolean(SOUND)) {
            startService()
        }
    }

    fun pauseBackgroundMusic() {
        if (sharedPref.getBoolean(SOUND)) {
            startService()
        }
    }

    fun vibrate() {
        if (sharedPref.getBoolean(VIBRATION)) {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                v!!.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                v!!.vibrate(60)
            }
        }
    }
}
