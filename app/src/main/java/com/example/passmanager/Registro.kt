package com.example.passmanager

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {
    private lateinit var botonRegistro: Button
    private lateinit var nombreU: EditText
    private lateinit var correoU: EditText
    private lateinit var contrasenyaU: EditText
    private lateinit var repeatPass: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        botonRegistro = findViewById(R.id.btnRegister)
        nombreU = findViewById(R.id.user)
        correoU = findViewById(R.id.mail)
        contrasenyaU = findViewById(R.id.password)
        repeatPass = findViewById(R.id.password_repeat)

        database = FirebaseDatabase.getInstance().getReference("Usuarios")

        botonRegistro.setOnClickListener {
            guardarDatosUsuarios()
            // Toast.makeText(this,"Test", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarDatosUsuarios() {
        val usuarioNombre = nombreU.text.toString()
        val usuarioCorreo = correoU.text.toString()
        val usuarioPass = contrasenyaU.text.toString()
        val repeatPass = repeatPass.text.toString()

        if (usuarioNombre.isEmpty() || usuarioCorreo.isEmpty() || usuarioPass.isEmpty() || usuarioPass.length < 8 && repeatPass != usuarioPass) {
            Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
            return
        } else {
            val idUsuario = database.push().key!!

            // Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show()

            val uBBDD = EstructuraBBDD(usuarioCorreo,usuarioNombre,usuarioPass)

            database.child(idUsuario).setValue(uBBDD).addOnCompleteListener {
                Log.d("Firebase", "Datos insertados")
                Toast.makeText(this, "Datos insertados",Toast.LENGTH_SHORT).show()

                nombreU.text.clear()
                correoU.text.clear()
                contrasenyaU.text.clear()
            }.addOnFailureListener { err ->
                Log.e("Firebase", "Error al insertar: ${err.message}")
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}