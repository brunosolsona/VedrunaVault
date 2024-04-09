package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import com.google.firebase.database.DatabaseReference

class create_password : AppCompatActivity() {
    private lateinit var botonCrearPassword: Button
    private lateinit var nombreApp: EditText
    private lateinit var passwordApp: EditText
    private lateinit var db2: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_password)

        botonCrearPassword = findViewById(R.id.btnCreatePass)
        nombreApp = findViewById(R.id.nombreApp)
        passwordApp = findViewById(R.id.passwordApp)
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")
        db2 = FirebaseDatabase.getInstance().getReference("apps_$nombreUsuario")

        botonCrearPassword.setOnClickListener {
            guardarPasswords()
            // Toast.makeText(this,"Test", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarPasswords() {
        val appName = nombreApp.text.toString()
        val appPassword = passwordApp.text.toString()
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")

        if (appName.isEmpty() || appPassword.isEmpty()) {
            Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
            return
        } else {
            // val idPass = db2.push().key!!

            val appsStruct = StructApps(appName,appPassword)

            db2.child(appName).setValue(appsStruct).addOnCompleteListener  {
                Log.d("Firebase", "Datos insertados")
                Toast.makeText(this, "Datos insertados",Toast.LENGTH_LONG).show()

                nombreApp.text.clear()
                passwordApp.text.clear()

                val intent = Intent(this@create_password, Bienvenido::class.java)
                intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
                startActivity(intent)
            }.addOnFailureListener { err ->
                Log.e("Firebase", "Error al insertar: ${err.message}")
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}