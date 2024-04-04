package com.example.passmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuariosAdapter(private val usuariosList: List<StructApps>) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val AppTextView: TextView = view.findViewById(R.id.nombreTextView)
        val PassTextView: TextView = view.findViewById(R.id.correoTextView)
        // Agrega más vistas si es necesario
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.AppTextView.text = usuario.appName
        holder.PassTextView.text = usuario.appPassword
        // holder.passTextView.text = usuario.usuarioPass
        // Configura aquí otras vistas del item si es necesario
    }

    override fun getItemCount() = usuariosList.size
}