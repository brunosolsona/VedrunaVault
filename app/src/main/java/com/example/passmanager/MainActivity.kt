package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clickLogin:Button = findViewById(R.id.Login)
        val clickRegister:Button = findViewById(R.id.register)
        clickLogin.setOnClickListener{
            val intent= Intent(this@MainActivity,Login::class.java)
            Toast.makeText(applicationContext,"Login", Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }
        clickRegister.setOnClickListener{
            val intent= Intent(this@MainActivity,Registro::class.java)
            Toast.makeText(applicationContext,"Register", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}