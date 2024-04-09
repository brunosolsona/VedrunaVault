package com.example.passmanager

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.reflect.TypeVariable

class UsuariosAdapter(private val usuariosList: List<StructApps>, private val nombreUsuario: String) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val AppTextView: TextView = view.findViewById(R.id.nombreTextView)
        val PassTextView: TextView = view.findViewById(R.id.correoTextView)
        val Btn_EditEntries: Button = view.findViewById(R.id.editData)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.AppTextView.text = usuario.appName
        holder.PassTextView.text = usuario.appPassword
        holder.Btn_EditEntries.setOnClickListener { v ->
            val intent = Intent(v.context, UpdateData::class.java)
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount() = usuariosList.size
}