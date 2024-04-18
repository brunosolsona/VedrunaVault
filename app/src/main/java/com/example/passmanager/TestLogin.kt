package com.example.passmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.passmanager.databinding.ActivityTestLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TestLogin : AppCompatActivity() {

    private lateinit var binding: ActivityTestLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityTestLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        passwordFocusListener()
        binding.submitButton.setOnClickListener { submitForm() }
    }

    private fun submitForm() {
        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        val databaseInstance = FirebaseDatabase.getInstance()
        database = databaseInstance.reference
        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val password = binding.passwordEditText.text.toString()
        val mail = binding.emailEditText.text.toString()
        if (validEmail && validPassword) {

            database.child("Usuarios").orderByChild("usuarioMail").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var objetoUsuario : EstructuraBBDD?
                    for (usuarioSnapshot in snapshot.children) {
                        objetoUsuario = usuarioSnapshot.getValue(EstructuraBBDD::class.java)
                        // Comprobar si la contraseña coincide
                        if (objetoUsuario?.usuarioPass == password && objetoUsuario?.usuarioMail == mail) {
                            val nombreUsuario = usuarioSnapshot.child("usuarioNombre").value.toString() // Obtener el nombre del usuario
                            // Toast.makeText(applicationContext, "$validPassword", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@TestLogin, Bienvenido::class.java)
                            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
                            startActivity(intent)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.emailContainer.helperText != null)
            message += "\n\nEmail: " + binding.emailContainer.helperText
        if(binding.passwordContainer.helperText != null)
            message += "\n\nContraseña: " +
                    binding.passwordContainer.helperText
        AlertDialog.Builder(this).setTitle("Formulario Inválido").setMessage(message).setPositiveButton("Ok"){ _,_ -> }.show()
    }

    private fun resetForm() {
        var message = "Email: " + binding.emailEditText.text
        message += "\nContraseña: " + binding.passwordEditText.text
        AlertDialog.Builder(this).setTitle("Formulario Enviado").setMessage(message).setPositiveButton("Ok"){ _,_ ->
                binding.emailEditText.text = null
                binding.passwordEditText.text = null
                binding.emailContainer.helperText = getString(R.string.required)
                binding.passwordContainer.helperText = getString(R.string.required)
        }.show()
    }

    private fun emailFocusListener() {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String? {
        val emailText = binding.emailEditText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Dirección de Email incorrecta"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordEditText.text.toString()
        if(passwordText.length < 8)
        {
            return "Mínimo 8 caracteres en el password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Debe contener una letra mayuscúla"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Debe contener una letra minuscúla"
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Debe contener un caracter especial (@#\$%^&+=)"
        }
        return null
    }
}