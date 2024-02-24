package com.example.jako

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class OutputActivity: AppCompatActivity(){
    private lateinit var output: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.output)

        output = findViewById(R.id.output_text)
        val receivedString = intent.getStringExtra("yourKey")
        output.text = receivedString
    }
}