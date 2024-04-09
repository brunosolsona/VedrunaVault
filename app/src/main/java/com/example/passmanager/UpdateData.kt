package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.passmanager.databinding.ActivityUpdateDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDataBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateButton.setOnClickListener {
            val appName = binding.appName.text.toString()
            val appPassword = binding.appPassword.text.toString()

            updateData(appName, appPassword)
        }
    }

    private fun updateData(appName: String, appPassword: String) {

        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")
        database = FirebaseDatabase.getInstance().getReference("apps_$nombreUsuario")

        val app = mapOf<String, String>(
            "appName" to appName,
            "appPassword" to appPassword
        )

        database.child(appName).updateChildren(app).addOnSuccessListener {
            binding.appName.text.clear()
            binding.appPassword.text.clear()
            Toast.makeText(this, "Success",Toast.LENGTH_LONG).show()

            val intent = Intent(this@UpdateData, Bienvenido::class.java)
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed",Toast.LENGTH_LONG).show()
        }
    }
}