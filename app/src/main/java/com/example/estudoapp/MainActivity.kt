package com.example.estudoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val tipoText = findViewById<TextView>(R.id.tipoText)
        val saldoText = findViewById<TextView>(R.id.saldoText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // 🔹 Recebe os dados vindos do LoginActivity
        val email = intent.getStringExtra("email") ?: "-"
        val resultado = intent.getStringExtra("resultado") ?: "Sem resultado"
        val renda = intent.getLongExtra("renda", 0)
        val gastos = intent.getLongExtra("gastos", 0)
        val poupanca = intent.getLongExtra("poupanca", 0)
        val idade = intent.getLongExtra("idade", 0)
        val investimentos = intent.getLongExtra("investimentos", 0)

        welcomeText.text = "Bem-vindo!"
        emailText.text = "📧 Email: $email"
        tipoText.text = "💡 Saúde Financeira: $resultado"
        saldoText.text = """
            💰 Renda: R$ $renda
            💳 Gastos: R$ $gastos
            🏦 Poupança: R$ $poupanca
            🎂 Idade: $idade
            📈 Investimentos: R$ $investimentos
        """.trimIndent()

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
