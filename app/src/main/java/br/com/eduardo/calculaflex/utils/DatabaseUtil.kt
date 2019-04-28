package br.com.eduardo.calculaflex.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DatabaseUtil {
    companion object { //TODO: O que eh companion object
        private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        init {
            firebaseDatabase.setPersistenceEnabled(true)
        }

        fun getDatabase(): FirebaseDatabase {
            return firebaseDatabase
        }

        fun saveToken(token: String?) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirebaseDatabase.getInstance().getReference("UsersTokens")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(token)
            }
        }
    }
}