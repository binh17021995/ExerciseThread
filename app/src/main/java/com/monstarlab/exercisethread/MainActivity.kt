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
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private var isUpdate: Boolean = false
    private var isKeep: Boolean = true
    private lateinit var btnMinus: Button
    private lateinit var btnPlus: Button
    private lateinit var tvInfor: TextView
    private lateinit var mHandler: Handler
    private var y1: Float = 0F
    private var y2: Float = 0F
    private val rnd = Random()
    private var color :Int =0

    companion object {
        const val MSG_UPDATE_NUMBER: Int = 100
        const val MSG_STOP_UPDATE_NUMBER: Int = 101
        const val MIN_DISTANCE: Int = 200
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
            color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
            number--
            tvInfor.text = "$number"
            checkFinish.cancel()
            checkFinish.start()
        }
        btnPlus.setOnClickListener {
            color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
            number++
            tvInfor.text = "$number"
            checkFinish.cancel()
            checkFinish.start()
        }

        btnMinus.setOnLongClickListener {
            color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
            isKeep = true
            minusThread()
            Log.i("BBB", " check Long: ")
            checkFinish.cancel()
            Log.i("BBB", " Tại đây diễn ra tn: ")
            false
        }
        btnPlus.setOnLongClickListener {
            color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
            isKeep = true
            plusThread()
            Log.i("BBB", " check Long Plus: ")
            checkFinish.cancel()
            Log.i("BBB", " Tại đây diễn ra tn: ")
            false
        }


        tvInfor.setOnTouchListener { _, event ->
            tvInfor.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
                    y1 = event.y
                    Log.i("BBB", " UP")

                }
                MotionEvent.ACTION_UP -> {
                    color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
                    isKeep = false
                    isUpdate = true
                    mHandler.postDelayed({
                        descendNumber()
                    },2000)

                    Log.i("BBB", " UP")

                }
                MotionEvent.ACTION_MOVE -> {
                    color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(200),rnd.nextInt(300))
                    y2 = event.y
                    val valueY: Float = y2 - y1
                    if (abs(valueY) > MIN_DISTANCE) {
                        if (y2 > y1) {
                            isKeep = true
                            minusThread()
                        } else {
                            isKeep = true
                            plusThread()
                        }
                    }
                    Log.i("BBB", "MOVE $y2 $y1")

                }

            }

            true
        }

    }

    private val checkFinish = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i("BBB", "vào đây không")
            isKeep = false
        }

        override fun onFinish() {
            Log.i("BBB", "Đây OnFinish")
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
                message.arg2 = color
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

    private fun plusThread() {
        Thread {
            while (isKeep) {
                number += 1
                val message = Message()
                message.what = MSG_UPDATE_NUMBER
                message.arg1 = number
                message.arg2 = color
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
                        tvInfor.setTextColor(msg.arg2)
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
                    message.arg2 = color
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    number += 1
                    val message = Message()
                    message.what = MSG_UPDATE_NUMBER
                    message.arg1 = number
                    message.arg2 = color
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)
        }.start()


    }


}



