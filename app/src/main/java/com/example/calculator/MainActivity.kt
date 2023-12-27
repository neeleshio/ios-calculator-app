package com.example.calculator

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var inputTextView: TextView
    private lateinit var clearBtn: Button
    private var decimalUsed: Boolean = false
    var leftDigits = ""
    var operator = ""
    var prevActiveBtn: Button? = null
    var rightHandTurn: Boolean = false
    var result = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputTextView = findViewById(R.id.input_textview)
        clearBtn = findViewById(R.id.btn_c)

        clearBtn.setOnClickListener {
            onClear(it)
        }

        inputTextView.setOnTouchListener {view, event ->
            when(event.action) {
                MotionEvent.ACTION_UP -> {
                    deleteDigit(view)
                }
            }
            true
        }
    }

    private fun deleteDigit(view: View) {
        inputTextView.text = inputTextView.text.toString().dropLast(1)
    }

    fun onDigit(view: View) {
        if(leftDigits != "" && rightHandTurn) {
            inputTextView.text = ""
            rightHandTurn = false
        }
        if (inputTextView.text.toString() == "0") {
            if((view as Button).text == "0") return
            inputTextView.text = (view as Button).text //overwriting the zero
        } else {
            inputTextView.append((view as Button).text) //appending the digits
        }
    }

    private fun onClear(view: View) {
        inputTextView.text = "0"
        decimalUsed = false
        leftDigits = ""
        operator = ""
        result = ""
    }

    fun onDecimalPoint(view: View) {
        if(!decimalUsed) {
            inputTextView.append(".")
            decimalUsed = true
        }
    }

    fun onOperator(view: View) {
        val activeBtn = view as Button
        operator = activeBtn.text.toString()
        leftDigits = inputTextView.text.toString()

        prevActiveBtn?.setBackgroundColor(Color.parseColor("#FFAC30"))
        prevActiveBtn?.setTextColor(Color.WHITE)

        view.setBackgroundColor(Color.WHITE)
        view.setTextColor(Color.parseColor("#FFAC30"))

        prevActiveBtn = activeBtn
        rightHandTurn = true
        decimalUsed = false
    }

    fun onPercent(view: View) {
        val inputText = inputTextView.text.toString()
        val percent = inputText.toDouble() / 100

        inputTextView.text = percent.toString()
    }

    fun onEqual(view: View) {
        var eval = 0.0
        prevActiveBtn?.setBackgroundColor(Color.parseColor("#FFAC30"))
        prevActiveBtn?.setTextColor(Color.WHITE)

        when(operator) {
            "+" -> eval = leftDigits.toDouble() + inputTextView.text.toString().toDouble()
            "-" -> eval = leftDigits.toDouble() - inputTextView.text.toString().toDouble()
            "x" -> eval = leftDigits.toDouble() * inputTextView.text.toString().toDouble()
            "/" -> eval = leftDigits.toDouble() / inputTextView.text.toString().toDouble()
        }

        if(eval.toString().split(".")[1] != "0") {
            result = eval.toString()
        } else {
            result = eval.toInt().toString()
        }

        inputTextView.text = result
    }
}