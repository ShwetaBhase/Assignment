package com.example.demoproject.Services

import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask
import android.app.Service
import android.os.Build
import android.os.Handler
import android.util.Log


class MyService : Service() {
    private val mHandler: Handler = Handler() //run on another Thread to avoid crash
    private var mTimer: Timer? = null //timer handling
    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override  fun onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer!!.cancel() else mTimer = Timer() //recreate new
        mTimer!!.scheduleAtFixedRate(TimeDisplay(), 0, notify.toLong()) //Schedule task
    }

    override    fun onDestroy() {
        super.onDestroy()
        mTimer!!.cancel() //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show()
    }

    //class TimeDisplay for handling task
    internal inner class TimeDisplay : TimerTask() {
        override fun run() {
            // run on another thread
            mHandler.post(Runnable { // display toast
                        Toast.makeText(this@MyService, "Service is running", Toast.LENGTH_SHORT).show()
            })
        }
    }

    companion object {
        const val notify = 600000 //interval between two services(Here Service run every 5 Minute)
    }
}
