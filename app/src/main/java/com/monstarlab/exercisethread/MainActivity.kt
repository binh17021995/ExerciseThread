package com.monstarlab.exercisethread

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private var isUpdate: Boolean = false
    private var isKeep: Boolean = true
    private lateinit var btnMinus: Button
    private lateinit var btnPlus: Button
    private lateinit var tvInfor: TextView
    private lateinit var mHandler: Handler
    var y1: Float = 0F
    var y2: Float = 0F

    companion object {
        const val MSG_UPDATE_NUMBER: Int = 100
        const val MSG_STOP_UPDATE_NUMBER: Int = 101
    }

    var number: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnMinus = findViewById(R.id.btn_minus)
        btnPlus = findViewById(R.id.btn_plus)
        tvInfor = findViewById(R.id.tv_infor)
        listenerHandler()
        var time: Long = 1000
        Thread.sleep(1000)

        btnMinus.setOnTouchListener { v, event ->
            btnMinus.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isKeep = true
                    isUpdate = false
                    minusThread(20)
                    Log.i("BBB", "action down: ")

                }
                MotionEvent.ACTION_UP -> {
                    isKeep = false
                    time =20
                    updateUI(20)
                    Log.i("BBB", "action up: ")

                }
            }
            true
        }


        btnPlus.setOnTouchListener { v, event ->
            btnPlus.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isKeep = true
                    isUpdate = false
                     plusThread(20)
                    Log.i("BBB", "action down: ")

                }
                MotionEvent.ACTION_UP -> {
                    isKeep = false
                    time =20
                    updateUI(20)
                    Log.i("BBB", "action up: ")

                }
            }
            true
        }
        tvInfor.setOnTouchListener { v, event -> tvInfor.onTouchEvent(event)
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                y1 =event.y
                Log.i("BBB", " UP")

            }
            MotionEvent.ACTION_UP -> {
                isKeep = false
                isUpdate = true
                updateUI(10)
                Log.i("BBB", " UP")

            }
            MotionEvent.ACTION_MOVE -> {
                y2 = event.y

                    if(y1>y2){
                        isKeep = true
                        plusThread(20)
                    } else {
                        isKeep = true
                        minusThread(20)
                    }
                Log.i("BBB", "MOVE $y2 $y1")

            }

        }

            true
        }

    }

    private fun minusThread(time : Long) {
        Thread {
            while (isKeep) {
                number -= 1
                val message = Message()
                message.what = MSG_UPDATE_NUMBER
                message.arg1 = number
                mHandler.sendMessage(message)
                try {
                    Thread.sleep(time)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)

        }.start()


    }

    private fun plusThread(time : Long) {
        Thread {
            while (isKeep) {
                number += 1
                val message = Message()
                message.what = MSG_UPDATE_NUMBER
                message.arg1 = number
                mHandler.sendMessage(message)
                try {
                    Thread.sleep(time)
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
                        tvInfor.setText(msg.arg1.toString())
                    }
                    MSG_STOP_UPDATE_NUMBER -> {
                        isKeep = false
                    }

                }
            }
        }
    }

    private fun updateUI(time : Long) {
        Thread {
            Thread.sleep(1500)
            while (isUpdate && number != 0) {
                if (number > 0) {
                    number -= 1
                    val message = Message()
                    message.what = MSG_UPDATE_NUMBER
                    message.arg1 = number
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(time)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    Thread.sleep(40)
                    number += 1
                    val message = Message()
                    message.what = MSG_UPDATE_NUMBER
                    message.arg1 = number
                    mHandler.sendMessage(message)
                    try {
                        Thread.sleep(time)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
            mHandler.sendEmptyMessage(MSG_STOP_UPDATE_NUMBER)
        }.start()


    }



}



