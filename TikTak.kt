package com.example.tiktakgame

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var PlayerTurn = 1

    var Player1Score = 0 //
    var Player2Score = 0 //


    var Player1Selection = ArrayList<Int>()
    var Player2Selection = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() // مخفی کردن actionBar

        for (i in 1..9) {
            var ButtonId = resources.getIdentifier("btnN" + i, "id", packageName)
            var ButtonDooz = findViewById<Button>(ButtonId)

            ButtonDooz.setOnClickListener {
                btnClick(it as Button, i)
            }
        }

        btnReset.setOnClickListener {
            resetGame()
        }

    }

    fun resetGame() {
        PlayerTurn = 1
        Player1Selection.clear()
        Player2Selection.clear()

        for (i in 1..9) {
            val buttonId = resources.getIdentifier("btnN$i", "id", packageName)
            val button = findViewById<Button>(buttonId).apply {
                text = ""
                isEnabled = true

                // تنظیم پارامترهای لایه‌بندی با عرض و ارتفاع دلخواه
                layoutParams = LinearLayout.LayoutParams(
                    0, // عرض (با weight کنترل می‌شود)
                    (177 * resources.displayMetrics.density).toInt(), // ارتفاع به dp (180dp)
                    1f // weight
                ).apply {
                    marginEnd = 4 // فاصله بین دکمه‌ها
                }
            }
        }
    }


    fun btnClick(Dokme: Button, Digit: Int) {
        if (PlayerTurn == 1) {
            Dokme.setText("X")
            Dokme.setTextColor(Color.YELLOW)
            Dokme.setTypeface(null, Typeface.BOLD)
            Dokme.textSize = 36f // اندازه فونت را بزرگتر کنید (مثلاً 36sp)
            Dokme.isEnabled = false
            PlayerTurn = 2
            Player1Selection.add(Digit)
            CheckTheWinner()
        } else if (PlayerTurn == 2) {
            Dokme.setText("O")
            Dokme.setTextColor(Color.RED)
            Dokme.setTypeface(null, Typeface.BOLD)
            Dokme.textSize = 36f // اندازه فونت را بزرگتر کنید (مثلاً 36sp)
            Dokme.isEnabled = false
            PlayerTurn = 1
            Player2Selection.add(Digit)
            CheckTheWinner()
        }
    }

    fun CheckTheWinner() {
        val winningCombinations = listOf(
            listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), // ردیف‌ها
            listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9), // ستون‌ها
            listOf(1, 5, 9), listOf(3, 5, 7)                   // قطرها
        )

        // بررسی برد بازیکن 1
        for (combination in winningCombinations) {
            if (Player1Selection.containsAll(combination)) {
                Player1Score++ // افزایش واقعی امتیاز
                txtP1.text = " Player1 = $Player1Score"
                Toast.makeText(this, "بازیکن 1 برنده شد!", Toast.LENGTH_SHORT).show()
                disableAllButtons() // غیرفعال کردن دکمه‌ها
                return
            }
        }

        // بررسی برد بازیکن 2
        for (combination in winningCombinations) {
            if (Player2Selection.containsAll(combination)) {
                Player2Score++ // افزایش واقعی امتیاز
                txtP2.text = " Player2 = $Player2Score"
                Toast.makeText(this, "بازیکن 2 برنده شد!", Toast.LENGTH_SHORT).show()
                disableAllButtons() // غیرفعال کردن دکمه‌ها
                return
            }
        }

        // بررسی تساوی (اگر همه خانه‌ها پر شده باشند)
        if (Player1Selection.size + Player2Selection.size == 9) {
            Toast.makeText(this, "بازی مساوی شد!", Toast.LENGTH_SHORT).show()
        }
    }

    // تابع برای غیرفعال کردن تمام دکمه‌ها
    private fun disableAllButtons() {
        for (i in 1..9) {
            val buttonId = resources.getIdentifier("btnN$i", "id", packageName)
            val button = findViewById<Button>(buttonId)
            button.isEnabled = false
        }
    }
}