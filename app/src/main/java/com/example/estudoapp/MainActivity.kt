package com.example.estudoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val tipoText = findViewById<TextView>(R.id.tipoText)
        val saldoText = findViewById<TextView>(R.id.saldoText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid

            db.collection("usuarios").document("usuarios").collection("usuarios").document(uid)

                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(this, "Erro ao escutar mudanças.", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val nome = snapshot.getString("nome") ?: "Usuário"
                        val email = snapshot.getString("email") ?: "-"
                        val tipo = snapshot.getString("tipo") ?: "Não definido"
                        val saldo = snapshot.getDouble("saldo") ?: 0.0

                        welcomeText.text = "Bem-vindo, $nome!"
                        emailText.text = "Email: $email"
                        tipoText.text = "Tipo de conta: $tipo"
                        saldoText.text = "Saldo: R$ %.2f".format(saldo)
                    } else {
                        welcomeText.text = "Usuário não encontrado."
                    }
                }
        } else {
            welcomeText.text = "Usuário não logado."
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
