package com.example.passmanager

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R
import com.example.passmanager.StructApps
import com.example.passmanager.UpdateData

class UsuariosAdapter(private val usuariosList: List<StructApps>, private val nombreUsuario: String) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val AppTextView: TextView = view.findViewById(R.id.nombreTextView)
        val PassTextView: TextView = view.findViewById(R.id.correoTextView)
        val cardView: CardView = view.findViewById(R.id.cardView)

        init {
            // Configura un OnClickListener en el CardView para manejar la selección
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = it.context
                    val usuario = usuariosList[position]
                    val intent = Intent(context, UpdateData::class.java).apply {
                        putExtra("NOMBRE_USUARIO", nombreUsuario)
                        putExtra("APP_NAME", usuario.appName)
                        putExtra("APP_PASSWORD", usuario.appPassword)
                    }
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.AppTextView.text = usuario.appName
        holder.PassTextView.text = usuario.appPassword

        // Actualiza el color de fondo del CardView en función del estado de selección
        if (usuario.isSelected) {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.colorPressed))
            val intent = Intent(holder.itemView.context, UpdateData::class.java)
            intent.putExtra("nombreApp", usuario.appName)
            holder.itemView.context.startActivity(intent)
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.white))
        }
    }
    override fun getItemCount() = usuariosList.size
}
