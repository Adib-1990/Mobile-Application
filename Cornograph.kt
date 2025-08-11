package com.example.timer

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() { // استفاده از AppCompatActivity به جای Activity
    private lateinit var countDownTimer: CountDownTimer
    private var timeElapsed: Long = 0
    private var timerHasStarted = false
    private val startTime = 5000L
    private val interval = 100L

    // UI Elements
    private lateinit var startButton: Button
    private lateinit var timerText: TextView
    private lateinit var timeElapsedText: TextView
    private lateinit var finalResultText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        startButton = findViewById(R.id.button)
        timerText = findViewById(R.id.timer)
        timeElapsedText = findViewById(R.id.timeElapsed)
        finalResultText = findViewById(R.id.finalResult)
        progressBar = findViewById(R.id.progressBar)

        // Setup UI
        setupUI()
    }

    private fun setupUI() {
        startButton.setOnClickListener {
            if (!timerHasStarted) {
                startTimer()
                startButton.text = "توقف"
                startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            } else {
                stopTimer()
                startButton.text = "شروع"
                startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                finalResultText.append("رکورد: $timeElapsed | زمان ذخیره شده: ${startTime - timeElapsed}\n")
            }
        }

        // Set initial values
        timerText.text = "زمان شروع: ${startTime}ms"
        progressBar.max = startTime.toInt()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(startTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeElapsed = startTime - millisUntilFinished
                timerText.text = "زمان باقیمانده: ${millisUntilFinished}ms"
                timeElapsedText.text = "زمان گذشته: ${timeElapsed}ms"
                progressBar.progress = timeElapsed.toInt()
            }

            override fun onFinish() {
                timerText.text = "پایان!"
                timeElapsedText.text = "زمان نهایی: $startTime"
                progressBar.progress = startTime.toInt()
                timerHasStarted = false
                startButton.text = "شروع مجدد"
            }
        }.start()
        timerHasStarted = true
    }

    private fun stopTimer() {
        countDownTimer.cancel()
        timerHasStarted = false
    }
}