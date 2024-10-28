package com.camilo.appcompras.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.camilo.appcompras.R
import com.camilo.appcompras.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment: Fragment(){

    private lateinit var mAuth: FirebaseAuth
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var signOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Referencias a los elementos del layout
        profileNameTextView = view.findViewById(R.id.userNameTextView)
        profileEmailTextView = view.findViewById(R.id.userEmailTextView)
        signOutButton = view.findViewById(R.id.signOutButton)

        // Obtener el usuario actual
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            // Mostrar la información del usuario en los TextViews
            profileNameTextView.text = currentUser.displayName ?: "Usuario"
            profileEmailTextView.text = currentUser.email ?: "Correo no disponible"
        }

        // Configurar el botón de cerrar sesión
        signOutButton.setOnClickListener {
            mAuth.signOut()
            // Redirigir a la pantalla de login después de cerrar sesión
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // Finaliza la actividad para que el usuario no pueda regresar
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}