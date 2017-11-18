package com.github.baishuai.filpboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val redBoard = findViewById<RedBoardView>(R.id.redBoardView)
        val button = findViewById<Button>(R.id.button)

        redBoard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.flip_board))
        button.setOnClickListener { redBoard.startFlip() }
    }
}
