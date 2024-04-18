package com.example.passmanager

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.passmanager.R
import com.example.passmanager.EstructuraBBDD
import com.google.firebase.database.*
import com.example.passmanager.databinding.ActivityTestRegisterBinding

class TestRegister : AppCompatActivity() {
    private lateinit var binding: ActivityTestRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userFocusListener()
        emailFocusListener()
        passwordFocusListener()
        password2FocusListener()
        binding.submitButton.setOnClickListener { submitForm() }
    }

    private fun submitForm()
    {
        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        binding.passwordRepeatContainer.helperText = validPassword2()
        binding.usernameContainer.helperText = validUsername()
        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validPassword2 = binding.passwordRepeatContainer.helperText == null
        val validUsername = binding.usernameContainer.helperText == null
        val passwordValidated = comparePassword()
        if (validEmail && validPassword && validPassword && validPassword2 && passwordValidated && validUsername) {
            sendForm()
        }
        else
        {
            invalidForm()
        }
    }

    private fun comparePassword() : Boolean
    {
        val password1 = binding.passwordEditText.text.toString()
        val password2 = binding.passwordRepeatEditText.text.toString()

        if(password1 != password2){
            return false
        }
        return true
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailEditText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Dirección de Email incorrecta"
        }
        return null
    }

    private fun passwordFocusListener()
    {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
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

    private fun password2FocusListener()
    {
        binding.passwordRepeatEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordRepeatContainer.helperText = validPassword2()
            }
        }
    }

    private fun validPassword2(): String?
    {
        val passwordRepeatText = binding.passwordRepeatEditText.text.toString()
        if(passwordRepeatText.length < 8)
        {
            return "Mínimo 8 caracteres en el password"
        }
        if(!passwordRepeatText.matches(".*[A-Z].*".toRegex()))
        {
            return "Debe contener una letra mayuscúla"
        }
        if(!passwordRepeatText.matches(".*[a-z].*".toRegex()))
        {
            return "Debe contener una letra minuscúla"
        }
        if(!passwordRepeatText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Debe contener un caracter especial (@#\$%^&+=)"
        }
        return null
    }

    private fun userFocusListener()
    {
        binding.usernameEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.usernameContainer.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String?
    {
        val userText = binding.usernameEditText.text.toString()

        if (userText.length <= 0){
            return "Debes introducir un nombre de usuario válido"
        }
        return null
    }

    private fun invalidForm()
    {
        var message = ""
        if(binding.emailContainer.helperText != null)
            message += "\n\nEmail: " + binding.emailContainer.helperText
        if(binding.passwordContainer.helperText != null)
            message += "\n\nContraseña: " +
                    binding.passwordContainer.helperText
        if(binding.passwordRepeatContainer.helperText != null)
            message += "\n\nSegunda Contraseña: " +
                    binding.passwordContainer.helperText
        if(binding.usernameContainer.helperText != null)
            message += "\n\nEdad: " +
                    binding.usernameContainer.helperText
        AlertDialog.Builder(this)
            .setTitle("Formulario Inválido")
            .setMessage(message)
            .setPositiveButton("Repetir"){ _,_ ->
            }
            .show()
    }

    private fun sendForm()
    {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        var message = "Email: " + binding.emailEditText.text
        message += "\nUsuario: " + binding.usernameEditText.text
        AlertDialog.Builder(this).setTitle("Formulario Enviado").setMessage(message).setPositiveButton("¡ENTRAR!"){ _,_ ->

            RegisterFunctions.addNewUser(this ,username, email, password){registered ->
                if(registered.result) {
                    val intentTerminos = Intent(this@TestRegister, TestLogin::class.java)
                    intentTerminos.putExtra("ManagerId", registered.parameter as Int)
                    startActivity(intentTerminos)
                }
                else {
                    Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
            .show()
    }
}