package com.monstarlab.exercisethread

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private var isUpdate: Boolean = false
    private var isKeep: Boolean = false
    private var isBackGround: Boolean = true
    private lateinit var btnMinus: Button
    private lateinit var btnPlus: Button
    private lateinit var tvInfor: TextView
    private lateinit var mHandler: Handler
    private var y1: Float = 0F
    private var y2: Float = 0F
    private var tb: Float = 0F
    private val rnd = Random()
    private var color: Int = 0

    companion object {
        const val MSG_UPDATE_NUMBER: Int = 100
        const val MSG_STOP_UPDATE_NUMBER: Int = 101
        const val PAUSE_THREAD: Int = 102
        const val MIN_DISTANCE: Float = 10F
    }

    private var number: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnMinus = findViewById(R.id.btn_minus)
        btnPlus = findViewById(R.id.btn_plus)
        tvInfor = findViewById(R.id.tv_infor)
        listenerHandler()

        btnMinus.setOnClickListener {
            isUpdate = false
            minusOnClick()
            checkFinish.cancel()
            checkFinish.start()
        }
        btnPlus.setOnClickListener {

            isUpdate = false
            plusOnClick()
            checkFinish.cancel()
            checkFinish.start()
        }
        btnMinus.setOnLongClickListener {
            isKeep = true
            minusThread()
            Thread.sleep(500)
            Log.i("BBB", " check Long: ")
            checkFinish.cancel()
            Log.i("BBB", " Tại đây diễn ra tn: ")
            false
        }
        btnPlus.setOnLongClickListener {
            isKeep = true
            plusThread()
            Thread.sleep(500)
            Log.i("BBB", " check Long Plus: ")
            checkFinish.cancel()
            isUpdate = false
            Log.i("BBB", " Tại đây diễn ra tn: ")
            false
        }
        tvInfor.setOnTouchListener { _, event ->
            tvInfor.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    y1 = event.rawY
                    isUpdate = false
                    Log.i("BBB", " UP")
                }
                MotionEvent.ACTION_UP -> {
                    isKeep = false
                    Thread.sleep(500)
                    isUpdate = true
                    mHandler.postDelayed({
                        descendNumber()
                    }, 1000)
                    Log.i("BBB", " UP")
                }
                MotionEvent.ACTION_MOVE -> {
                    y2 = event.y
                    if (y2 > y1) {
                        number--
                        tvInfor.text = "$number"
                        y1 = y2
                    } else {
                        number++
                        tvInfor.text = "$number"
                        y1 = y2
                    }

                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.i("BBB", "action cancel")
                }

            }

            true
        }

    }

    private val checkFinish = object : CountDownTimer(1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i("BBB", "vào đây không")
            isKeep = false
            isBackGround = false
            isUpdate = false
        }

        override fun onFinish() {
            Log.i("BBB", "Đây OnFinish")
            Thread.sleep(500)
            isUpdate = true
            descendNumber()
        }
    }

    private fun minusThread() {
        Thread {
            Log.i("BBB", "như tn")
            while (isKeep) {
                number -= 1
                val message = Message()
                message.what = MSG_UPDATE_NUMBER
                message.arg1 = number
                mHandler.sendMessage(message)
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)
        }.start()

    }

    private fun minusOnClick() {
        number--
        tvInfor.text = "$number"
        if (number % 100 == 0) {
            color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(300), rnd.nextInt(200))
            tvInfor.setTextColor(color)
        }
    }

    private fun plusOnClick() {
        number++
        tvInfor.text = "$number"
        if (number % 100 == 0) {
            color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(300), rnd.nextInt(200))
            tvInfor.setTextColor(color)
        }
    }

    private fun plusThread() {
        Thread {
            while (isKeep) {
                number += 1
                val message = Message()
                message.what = MSG_UPDATE_NUMBER
                message.arg1 = number
                mHandler.sendMessage(message)
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)
        }.start()

    }

    private fun listenerHandler() {
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MSG_UPDATE_NUMBER -> {
                        isUpdate = true
                        tvInfor.text = msg.arg1.toString()
                    }
                    MSG_STOP_UPDATE_NUMBER -> {
                        isKeep = false
                    }

                }
            }
        }
    }

    private fun descendNumber() {
        Thread {
            while (isUpdate && number != 0) {
                if (number > 0) {
                    number -= 1
                    Log.i("BBB", "descend")
                    val message = Message()
                    message.what = MSG_UPDATE_NUMBER
                    message.arg1 = number
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(40)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    number += 1
                    val message = Message()
                    message.what = MSG_UPDATE_NUMBER
                    message.arg1 = number
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(40)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)
        }.start()

    }


}



