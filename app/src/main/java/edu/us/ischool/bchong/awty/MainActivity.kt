package edu.us.ischool.bchong.awty

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    private lateinit var phoneNum: EditText
    private lateinit var spamMin: EditText
    private lateinit var startButton: Button
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var formValidation = hashMapOf("phoneNumFilled" to false, "spamMinFilled" to false)

        phoneNum = findViewById(R.id.phone_num)
        spamMin = findViewById(R.id.spam_min)
        startButton = findViewById(R.id.start_button)

        phoneNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                formValidation["phoneNumFilled"] = p0.isNotEmpty()
                startButton.isEnabled = formValidation["phoneNumFilled"] == true &&
                        formValidation["spamMinFilled"] == true
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        spamMin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (p0.toString().startsWith("0"))
                    p0.clear()
                formValidation["spamMinFilled"] = p0.isNotEmpty()
                startButton.isEnabled = formValidation["phoneNumFilled"] == true &&
                        formValidation["spamMinFilled"] == true
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        startButton.setOnClickListener {
            if (startButton.text.toString() == "Start") {
                startButton.text = "Stop"
                val period: Long = spamMin.text.toString().toLong() * 60 * 1000
                val phoneNumString = phoneNum.text.toString()
                val phoneFormat = "(" + phoneNumString.substring(0, 3) + ") " +
                        phoneNumString.substring(3, 6) + "-" + phoneNumString.substring(6, 10)
                val message = "$phoneFormat: Are we there yet?"
                timer = fixedRateTimer("awty", false, 0L, period) {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                timer.cancel()
                startButton.text = "Start"
            }
        }



    }
}
