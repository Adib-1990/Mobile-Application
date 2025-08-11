package com.example.barcodereader

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun HandleClick(arg0: View) {
        val intent = Intent("com.google.zxing.client.android.SCAN")
        when (arg0.id) {
            R.id.butQR -> intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            R.id.butProd -> intent.putExtra("SCAN_MODE", "PRODUCT_MODE")
            R.id.butOther -> intent.putExtra(
                "SCAN_FORMATS",
                "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,AZTEC,PDF_417"
            )
        }
        try {
            startActivityForResult(intent, 0)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Please install the Zxing Barcode Scanner app", Toast.LENGTH_LONG)
                .show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == 0) {
            val tvStatus = findViewById<View>(R.id.tvStatus) as TextView
            val tvResult = findViewById<View>(R.id.tvResult) as TextView
            if (resultCode == RESULT_OK) {
                tvStatus.text = intent!!.getStringExtra("SCAN_RESULT_FORMAT")
                val scanResult = intent.getStringExtra("SCAN_RESULT") ?: ""
                tvResult.text = scanResult

                // فعال کردن Linkify برای URL‌ها
                if (android.util.Patterns.WEB_URL.matcher(scanResult).matches()) {
                    tvResult.autoLinkMask = android.text.util.Linkify.WEB_URLS
                    tvResult.linksClickable = true
                }
            } else if (resultCode == RESULT_CANCELED) {
                tvStatus.text = "Press a button to start a Scan."
                tvResult.text = "Scan Cancelled."
            }
        }
    }
}