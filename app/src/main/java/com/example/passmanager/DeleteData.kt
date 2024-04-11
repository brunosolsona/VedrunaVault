package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.passmanager.databinding.ActivityDeleteDataBinding
import com.example.passmanager.databinding.ActivityUpdateDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteData : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteDataBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deleteButton.setOnClickListener {
            val appName = binding.DeleteAppName.text.toString()

            deleteData(appName)
        }
    }

    private fun deleteData(appName: String) {
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")
        database = FirebaseDatabase.getInstance().getReference("apps_$nombreUsuario")

        database.child(appName).removeValue().addOnSuccessListener {
            binding.DeleteAppName.text.clear()
            Toast.makeText(this,"$appName", Toast.LENGTH_LONG).show()

            val intent = Intent(this@DeleteData, Bienvenido::class.java)
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this,"Nigger2", Toast.LENGTH_LONG).show()
        }
    }
}