package com.example.dongcalculate

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.NumberFormat


class MainActivity : Activity() {
    // Section 1:  معرفی ویجت های اپلیکیشن به عنوان ویژگی کلاس مادر
    private var txtAmount: EditText? = null
    private var txtPeople: EditText? = null
    private var txtTipOther: EditText? = null
    private var rdoGroupTips: RadioGroup? = null
    private var btnCalculate: Button? = null
    private var btnReset: Button? = null

    // Section 2 : معرفی ویجت های اپلیکیشن به عنوان ویژگی کلاس مادر
    private var txtTipAmount: TextView? = null
    private var txtTotalToPay: TextView? = null
    private var txtTipPerPerson: TextView? = null
    private var radioCheckedid = -1
    private val mlogic: NumberPicker? = null

    //private NumberPickerLogic mlogic;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Section 3:  دریافت ویجت موردنظر و جانمایی در متغیر مربوطه
        txtAmount = findViewById<View>(R.id.txtAmount) as EditText


        // Section 4:   تمرکز رابط کاربری بر روی ویجت مبلغ کل پس از اجرای برنامه
        txtAmount!!.requestFocus()
        txtPeople = findViewById<View>(R.id.txtPeople) as EditText
        txtPeople!!.setText(DEFAULT_NUM_People.toString()) // PEOPLE
        txtTipOther = findViewById<View>(R.id.txtTipOther) as EditText
        rdoGroupTips = findViewById<View>(R.id.RadioGroupTips) as RadioGroup
        btnCalculate = findViewById<View>(R.id.btnCalculate) as Button


        // Section 5:  فعال کردن دکمه محاسبه پس از دریافت اطلاعاتی از قبیل: مبلغ کل و تعداد نفرات
        btnCalculate!!.isEnabled = false
        btnReset = findViewById<View>(R.id.btnReset) as Button


        // Section 6:  دریافت ویجت موردنظر و جانمایی در متغیر مربوطه
        txtTipAmount = findViewById<View>(R.id.txtTipAmount) as TextView
        txtTotalToPay = findViewById<View>(R.id.txtTotalToPay) as TextView
        txtTipPerPerson = findViewById<View>(R.id.txtTipPerPerson) as TextView


        // Section 7:  فعال شدن قابلیت انتخاب درصد موردنظر در پایان کار
        txtTipOther!!.isEnabled = false

        // بخش دوم ...
        rdoGroupTips!!.setOnCheckedChangeListener { group, CheckedId ->
            if (CheckedId == R.id.radioFifteen || CheckedId == R.id.radioTwenty) {
                txtTipOther!!.isEnabled = false
                btnCalculate!!.isEnabled =
                    txtAmount!!.text.length > 0 && txtPeople!!.text.length > 0
            }
            if (CheckedId == R.id.radioOther) {
                txtTipOther!!.isEnabled = true
                txtTipOther!!.requestFocus()
                btnCalculate!!.isEnabled =
                    txtAmount!!.text.length > 0 && txtPeople!!.text.length > 0 && txtTipOther!!.text.length > 0
            }
            radioCheckedid = CheckedId
        }
        val mKeyListener =
            View.OnKeyListener { v, keyCode, event ->
                when (v.id) {
                    R.id.txtAmount, R.id.txtPeople -> btnCalculate!!.isEnabled =
                        (txtAmount!!.text.length > 0
                                && txtPeople!!.text.length > 0)
                    R.id.txtTipOther -> btnCalculate!!.isEnabled =
                        txtAmount!!.text.length > 0 && txtPeople!!.text.length > 0 && txtTipOther!!.text.length > 0
                }
                false
            }
        val mClickListener =
            View.OnClickListener { v ->
                if (v.id == R.id.btnCalculate) {
                    calculate()
                } else {
                    reset()
                }
            }
        btnCalculate!!.setOnClickListener(mClickListener)
        btnReset!!.setOnClickListener(mClickListener)
    }

    private fun reset() {
        txtTipAmount!!.text = ""
        txtTotalToPay!!.text = ""
        txtTipPerPerson!!.text = ""
        txtAmount!!.setText("")
        txtPeople!!.setText(DEFAULT_NUM_People.toString())
        txtTipOther!!.setText("")
        rdoGroupTips!!.clearCheck()
        rdoGroupTips!!.check(R.id.radioFifteen)
        txtAmount!!.requestFocus()
    }

    private fun calculate() {
        val billAmount = txtAmount!!.text.toString().toDouble()
        val totalPeople = txtPeople!!.text.toString().toDouble()
        var precentage: Double? = null
        var isError = false
        if (billAmount < 1.0) {
            showErrorAlert("Enter a valid Total Amount", txtAmount!!.id)
            isError = true
        }
        if (totalPeople < 1.0) {
            showErrorAlert("Enter a valid number of people", txtPeople!!.id)
            isError = true
        }
        if (radioCheckedid == -1) {
            radioCheckedid = rdoGroupTips!!.checkedRadioButtonId
        }
        if (radioCheckedid == R.id.radioFifteen) {
            precentage = 15.00
        } else if (radioCheckedid == R.id.radioTwenty) {
            precentage = 20.00
        } else if (radioCheckedid == R.id.radioOther) {
            precentage = txtTipOther!!.text.toString().toDouble()
            if (precentage < 1.0) {
                showErrorAlert("Enter a valid Tip precentage", txtTipOther!!.id)
                isError = true
            }
        }
        if (!isError) {
            val tipAmount = billAmount * precentage!! / 100
            val totalToPay = billAmount + tipAmount
            val perPersonPays = totalToPay / totalPeople
            txtTipAmount!!.text = formatter.format(tipAmount)
            txtTotalToPay!!.text = formatter.format(totalToPay)
            txtTipPerPerson!!.text = formatter.format(perPersonPays)
        }
    }

    private fun showErrorAlert(errorMessage: String, fieldId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setNeutralButton(
                "Close"
            ) { dialog, which -> findViewById<View>(fieldId).requestFocus() }.show()
    }

    // متد برای کاهش تعداد افراد
    fun decrement(view: View?) {
        var currentValue = txtPeople!!.text.toString().toInt()
        if (currentValue > 1) {
            currentValue--
            txtPeople!!.setText(currentValue.toString())
        }
    }

    // متد برای افزایش تعداد افراد
    fun increment(view: View?) {
        var currentValue = txtPeople!!.text.toString().toInt()
        currentValue++
        txtPeople!!.setText(currentValue.toString())
    }

    companion object {
        const val DEFAULT_NUM_People = 3
        val formatter = NumberFormat.getCurrencyInstance()
    }
}