package com.hnsh.dialogue.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.dosmono.logger.Logger
import com.hnsh.dialogue.MainActivity
import com.hnsh.dialogue.R
import com.hnsh.dialogue.utils.DateUtil
import com.jaeger.library.StatusBarUtil
@SuppressLint("HandlerLeak")
class MenuActivity : AppCompatActivity() {
    private val MSG_KEY = 1
    private var stopThread = false
    lateinit var cl_dialogue: ConstraintLayout
    lateinit var tv_time: TextView
    lateinit var tv_weekday: TextView
    lateinit var tv_date: TextView

    inner class TimeThread : Thread() {
        override fun run() {
            do {
                try {
                    val msg = Message()
                    msg.what = MSG_KEY
                    mHandler.sendMessage(msg)
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace();
                }
            } while (!stopThread)
        }
    }

    var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_KEY -> {
                    val time = DateUtil.getInstance().time
                    val times = time.split(",")
                    tv_time.text = times[1]
                    tv_date.text = times[0]
                    tv_weekday.text = times[2]
                }
                else -> Logger.d("others")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucentForImageView(this, 0, null)
        setContentView(R.layout.activity_menu)
        cl_dialogue = findViewById(R.id.cl_dialogue)
        tv_time=findViewById(R.id.tv_time)
        tv_date=findViewById(R.id.tv_date)
        tv_weekday=findViewById(R.id.tv_weekday)
        cl_dialogue.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        TimeThread().start()
        Logger.d("10px=====${px2dip(10f)}dp")
    }

    fun px2dip(pxValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopThread = true
    }
}
