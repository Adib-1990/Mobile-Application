package com.example.examquiz

import android.graphics.Color
//import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questions = listOf(
        Question("زبان بومی اندروید کدام است؟", listOf("کاتلین", "پایتون", "سی پلاس", "سی شارپ"), 0),
        Question("کدام زبان جدیدتر است؟", listOf("سی پلاس", "گرووی", "پاسکال", "سی شارپ"), 1),
        Question("کدام زبان مناسب هوش مصنوعی است؟", listOf("کاتلین", "جاوا", "سی پلاس", "پایتون"), 3),
        Question("مناسبترین محیط برای توسعه اپلیکیشنهای اندرویدی کدام است؟", listOf("Eclips", "X Code", "Android Studio", "IntelliJ"), 2)
    )

    private var currentQuestionIndex = 0
    private val userAnswers = MutableList(questions.size) { -1 }
    private var allQuestionsAnswered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayQuestion()

        btnNext.setOnClickListener {
            saveAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                displayQuestion()
            } else {
                checkAllAnswers()
            }
        }

        btnPre.setOnClickListener {
            saveAnswer()
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion()
            }
        }
    }

    private fun displayQuestion() {
        val question = questions[currentQuestionIndex]

        txtExamTitle.text = question.text
        rb1.text = question.options[0]
        rb2.text = question.options[1]
        rb3.text = question.options[2]
        rb4.text = question.options[3]

        rg.clearCheck()

        if (userAnswers[currentQuestionIndex] != -1) {
            when (userAnswers[currentQuestionIndex]) {
                0 -> rb1.isChecked = true
                1 -> rb2.isChecked = true
                2 -> rb3.isChecked = true
                3 -> rb4.isChecked = true
            }
        }

        btnPre.isEnabled = currentQuestionIndex > 0
        btnNext.text = if (currentQuestionIndex == questions.size - 1) "پایان آزمون" else "بعدی"
    }

    private fun saveAnswer() {
        userAnswers[currentQuestionIndex] = when {
            rb1.isChecked -> 0
            rb2.isChecked -> 1
            rb3.isChecked -> 2
            rb4.isChecked -> 3
            else -> -1
        }
    }

    private fun checkAllAnswers() {
        // بررسی آیا همه سوالات پاسخ داده شده اند
        allQuestionsAnswered = userAnswers.none { it == -1 }

        if (allQuestionsAnswered) {
            showResultDialog()
        } else {
            txtResult.text = "لطفاً به تمام سوالات پاسخ دهید"
            txtResult.setTextColor(Color.RED)
        }
    }

    private fun showResultDialog() {
        val correctAnswers = calculateCorrectAnswers()
        val percentage = (correctAnswers * 100) / questions.size

        val resultMessage = """
            نتیجه آزمون:
            تعداد سوالات: ${questions.size}
            پاسخ‌های صحیح: $correctAnswers
            پاسخ‌های اشتباه: ${questions.size - correctAnswers}
            درصد موفقیت: $percentage%
            
            ${getAnswerDetails()}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("کارنامه آزمون")
            .setMessage(resultMessage)
            .setPositiveButton("تایید") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun calculateCorrectAnswers(): Int {
        var correct = 0
        questions.forEachIndexed { index, question ->
            if (userAnswers[index] == question.correctAnswer) {
                correct++
            }
        }
        return correct
    }

    private fun getAnswerDetails(): String {
        val details = StringBuilder("\nجزئیات پاسخ‌ها:\n")
        questions.forEachIndexed { index, question ->
            details.append("\nسوال ${index + 1}: ")
            details.append(if (userAnswers[index] == question.correctAnswer) "✅" else "❌")
            details.append(" (پاسخ صحیح: ${question.options[question.correctAnswer]})")
        }
        return details.toString()
    }

    data class Question(val text: String, val options: List<String>, val correctAnswer: Int)
}