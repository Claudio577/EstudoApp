package com.example.estudoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // 🔹 LOGIN EXISTENTE
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val db = Firebase.firestore

                        // 🔹 Busca o documento do Firestore pelo campo "email" (igual ao site)
                        db.collection("usuarios")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    val doc = documents.documents[0]
                                    val resultado = doc.getString("resultado") ?: "Sem resultado"
                                    val renda = doc.getLong("renda") ?: 0
                                    val gastos = doc.getLong("gastos") ?: 0
                                    val poupanca = doc.getLong("poupanca") ?: 0
                                    val idade = doc.getLong("idade") ?: 0
                                    val investimentos = doc.getLong("investimentos") ?: 0

                                    // 🔹 Envia os dados para MainActivity
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("email", email)
                                    intent.putExtra("resultado", resultado)
                                    intent.putExtra("renda", renda)
                                    intent.putExtra("gastos", gastos)
                                    intent.putExtra("poupanca", poupanca)
                                    intent.putExtra("idade", idade)
                                    intent.putExtra("investimentos", investimentos)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "⚠️ Nenhum dado encontrado no Firestore para este email.", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Erro ao buscar dados: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro no login: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 CRIAR NOVO USUÁRIO (só no Auth — sem alterar o Firestore do site)
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuário criado com sucesso! Agora faça login.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao criar usuário: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
