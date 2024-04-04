package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var Btn_buscaRegistro: Button
    private lateinit var correoL: EditText
    private lateinit var passL: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        correoL = findViewById(R.id.loginMail)
        passL = findViewById(R.id.loginPassword)
        Btn_buscaRegistro = findViewById(R.id.loginBtnLogin)

        // Utiliza el correo electrónico como la clave principal en la base de datos
        database = FirebaseDatabase.getInstance().getReference("Usuarios")

        Btn_buscaRegistro.setOnClickListener {
            verificaDatosLogin()
        }
    }

    private fun verificaDatosLogin() {
        val correo = correoL.text.toString().trim()
        val password = passL.text.toString().trim()

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el correo electrónico y la contraseña", Toast.LENGTH_LONG).show()
            return
        }

        // Buscar en todos los usuarios
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var usuarioEncontrado = false
                for (usuarioSnapshot in snapshot.children) {
                    val correoDB = usuarioSnapshot.child("usuarioMail").value.toString()

                    if (correoDB.equals(correo, ignoreCase = true)) {
                        usuarioEncontrado = true
                        // Comprobar si la contraseña coincide
                        val dbPassword = usuarioSnapshot.child("usuarioPass").value.toString()
                        if (dbPassword == password) {
                            val nombreUsuario = usuarioSnapshot.child("usuarioNombre").value.toString() // Obtener el nombre del usuario
                            Toast.makeText(applicationContext, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@Login, Bienvenido::class.java)
                            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
                            startActivity(intent)
                            break
                        } else {
                            Toast.makeText(applicationContext, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                            break
                        }
                    }
                }
                if (!usuarioEncontrado) {
                    Toast.makeText(applicationContext, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, Registro::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

}