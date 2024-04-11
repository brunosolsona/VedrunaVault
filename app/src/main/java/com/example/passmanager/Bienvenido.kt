package com.example.passmanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Bienvenido : AppCompatActivity() {
    private lateinit var usuariosRecyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter
    // private lateinit var usuariosAdapter2: UsuariosAdapter
    private lateinit var database: DatabaseReference
    // private lateinit var database2: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")
        val txtSaludo = findViewById<TextView>(R.id.txtSaludo)
        val BtnCrearPass = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val BtnDeleteEntry = findViewById<FloatingActionButton>(R.id.floatingActionDeleteButton)

        txtSaludo.text = "¡Hola, $nombreUsuario!"
        usuariosRecyclerView = findViewById(R.id.recyclerViewUsuarios)
        usuariosRecyclerView.layoutManager = LinearLayoutManager(this)
        // usuariosRecyclerView.setHasFixedSize(true) // Si sabes que el contenido no va a cambiar el tamaño del RecyclerView

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("apps_$nombreUsuario")

        // Leer los datos de la base de datos y actualizar la UI
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val passList = mutableListOf<StructApps>()
                for (usuarioSnapshot in dataSnapshot.children) {
                    val usuario = usuarioSnapshot.getValue(StructApps::class.java)
                    usuario?.let { passList.add(it) }
                }
                usuariosAdapter = UsuariosAdapter(passList, nombreUsuario ?: "")
                usuariosRecyclerView.adapter = usuariosAdapter
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        BtnCrearPass.setOnClickListener {
            val intent = Intent(this@Bienvenido, UpdateData::class.java)
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
            startActivity(intent)
        }
        // Toast.makeText(this,"Error de base de datos, $usuariosAdapter, $usuariosRecyclerView",Toast.LENGTH_LONG).show()

        BtnDeleteEntry.setOnClickListener {
            val intent2 = Intent(this@Bienvenido, DeleteData::class.java)
            intent2.putExtra("NOMBRE_USUARIO", nombreUsuario)
            startActivity(intent2)
        }
    }
}