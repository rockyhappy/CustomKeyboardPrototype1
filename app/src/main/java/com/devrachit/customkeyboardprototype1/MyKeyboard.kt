package com.devrachit.customkeyboardprototype1

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.devrachit.customkeyboardprototype1.databinding.KeyboardLayoutBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyKeyboard : InputMethodService() {
    private lateinit var keyboardBinding: KeyboardLayoutBinding
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.api_key
    )
    var inputText = " "
    override fun onCreateInputView(): View {
        keyboardBinding = KeyboardLayoutBinding.inflate(layoutInflater)


        val buttonIds = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0,
            R.id.btnQ,R.id.btnW,R.id.btnE,R.id.btnR,R.id.btnT,R.id.btnY,R.id.btnU,R.id.btnI,R.id.btnO,R.id.btnP,
            R.id.btnA,R.id.btnS,R.id.btnD,R.id.btnF,R.id.btnG,R.id.btnH,R.id.btnJ,R.id.btnK,R.id.btnL,
            R.id.btnZ,R.id.btnX,R.id.btnC,R.id.btnV,R.id.btnB,R.id.btnN,R.id.btnM,R.id.btnDot,R.id.btnComma
        )

        val sample1=keyboardBinding.root.findViewById<TextView>(R.id.sample1)
        sample1.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.commitText(sample1.text.toString(), 1)
        }
        val sample2=keyboardBinding.root.findViewById<TextView>(R.id.sample2)
        sample2.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.commitText(sample2.text.toString(), 1)
        }
        val sample3=keyboardBinding.root.findViewById<TextView>(R.id.sample3)
        sample3.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.commitText(sample3.text.toString(), 1)
        }




            for (buttonId in buttonIds) {
            val button = keyboardBinding.root.findViewById<Button>(buttonId)
            button.setOnClickListener {
                val inputConnection = currentInputConnection
                inputConnection?.commitText(button.text.toString(), 1)
            }
        }
    
        keyboardBinding.btnBackSpace.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            return@setOnClickListener
        
        }
    
        keyboardBinding.btnEnter.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            val text=inputConnection?.getTextBeforeCursor(100,0)?.toString()?.trim()
            if(text!=null){
                inputText=text.toString()
                getRecommendations()
            }
            return@setOnClickListener
        
        }
    
        keyboardBinding.btnSpace.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE))
            val text=inputConnection?.getTextBeforeCursor(100,0)?.toString()?.trim()
            if(text!=null){
                inputText=text.toString()
                getRecommendations()
            }
            return@setOnClickListener
        
        }
        return keyboardBinding.root
    }
    private fun getRecommendations() {
        CoroutineScope(Dispatchers.IO).launch {
            val prompt = "Give me three different suggestion each separated by a space only no other formatting and containing maximum 1 words not more than that which i might say after : ${inputText}"
            val response = generativeModel.generateContent(prompt)
            val words = response.text.toString().split(" ")
            keyboardBinding.sample1.text=words[0]
            keyboardBinding.sample2.text=words[1]
            keyboardBinding.sample3.text=words[2]
        }
    }
    
}










