package com.example.ratingbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val barChangeListener =
            OnRatingBarChangeListener { rBar, fRating, fromUser ->
                val rating = fRating.toInt()
                var message: String? = null
                when (rating) {
                    1 -> message = " خیییلی بد بودیم !!! "
                    2 -> message = " بد بودیم !! "
                    3 -> message = " معمولی بودیم "
                    4 -> message = " خوب بودیم ☺ "
                    5 -> message = " عالی بودیم ☺ ☺ "
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }

        val sBar = findViewById<View>(R.id.serviceBar) as RatingBar
        sBar.onRatingBarChangeListener = barChangeListener
        val pBar = findViewById<View>(R.id.priceBar) as RatingBar
        pBar.onRatingBarChangeListener = barChangeListener

        val doneButton = findViewById<View>(R.id.doneButton) as Button
        doneButton.setOnClickListener {
            val message = String.format(
                "Final Answer: Service %.0f/%d , Price %.0f/%d\nThanks!",
                sBar.rating, sBar.numStars,
                pBar.rating, pBar.numStars
            )
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}