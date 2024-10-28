package com.camilo.appcompras.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camilo.appcompras.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }
        mAuth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailRegisterEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordRegisterEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            if (emailEditText.text.isEmpty() || passwordEditText.text.isEmpty() || confirmPasswordEditText.text.isEmpty()) {
                Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email= emailEditText.text
            val pass= passwordEditText.text
            val confirmPass= confirmPasswordEditText.text
            if (pass.toString() != confirmPass.toString()) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email.toString(), pass.toString()).addOnSuccessListener {
                Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }
        }


    }
}