package com.example.passmanager

import android.widget.Toast
import android.content.Context
import com.example.passmanager.EstructuraBBDD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class RegisterFunctions {
    companion object {

        data class Result(val result: Boolean, val parameter: Int)

        fun addNewUser(context: Context, usuarioMail: String, usuarioNombre: String, usuarioPass: String, callback: (Result) -> Unit){
            lateinit var database: DatabaseReference
            val databaseInstance = FirebaseDatabase.getInstance()
            database = databaseInstance.reference
            emailAlreadyExists(usuarioMail){ itExists ->
                if(itExists){
                    showMessage(context,"El correo ya existe")
                }
                else{
                    getId { managerId ->
                        if (managerId > 0) {
                            val managerObject = EstructuraBBDD(usuarioNombre, usuarioMail, usuarioPass)
                            val usersRef = database.child("Usuarios")
                            val newChildRef = usersRef.push()

                            newChildRef.setValue(managerObject).addOnCompleteListener {
                                val finalResult = Result(result = true, parameter = managerId)
                                callback(finalResult)
                                showMessage(context, "Registro con éxito")
                            }
                        } else {
                            showMessage(context, "No se ha encontrado ID de Usuario")
                        }
                    }

                }
            }
        }

        fun emailAlreadyExists(valorAComparar: String, callback: (Boolean) -> Unit) {
            try {
                lateinit var database: DatabaseReference
                val databaseInstance = FirebaseDatabase.getInstance()
                database = databaseInstance.reference
                database.child("Users").orderByChild("email").equalTo(valorAComparar)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            callback(dataSnapshot.exists())
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            callback(databaseError.code == DatabaseError.NETWORK_ERROR)
                        }
                    })
            } catch (e: Exception) {
                callback(false)
            }
        }

        fun getId(callback: (Int) -> Unit) {
            lateinit var database: DatabaseReference
            val databaseInstance = FirebaseDatabase.getInstance()
            database = databaseInstance.reference
            database.child("Users").orderByChild("idManager").limitToLast(1)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val lastId = dataSnapshot.children.lastOrNull()?.child("idManager")?.getValue(Int::class.java) ?: 0
                        val newId = lastId + 1
                        callback(newId)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        callback(-1)
                    }
                })
        }


        private fun showMessage(context: Context, mensaje: String) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}