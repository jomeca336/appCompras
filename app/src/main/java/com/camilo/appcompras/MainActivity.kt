package com.camilo.appcompras

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.camilo.appcompras.entity.Item
import com.camilo.appcompras.fragment.HomeFragment
import com.camilo.appcompras.fragment.ProfileFragment
import com.camilo.appcompras.fragment.ShoppingFragment
import com.camilo.appcompras.provider.ItemProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var createButton: FloatingActionButton
    private lateinit var mItemProvider: ItemProvider
    private lateinit var mAuth: FirebaseAuth

    private var quantity = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        createButton = findViewById<FloatingActionButton>(R.id.createButton)
        mItemProvider = ItemProvider()
        mAuth = FirebaseAuth.getInstance()
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    loadFragment(HomeFragment())
                    showFloatingActionButton(true)
                    true
                }
                R.id.menu_shopping_list -> {
                    loadFragment(ShoppingFragment())
                    showFloatingActionButton(false)
                    true
                }
                R.id.menu_profile -> {
                    loadFragment(ProfileFragment())
                    showFloatingActionButton(false)
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            bottomNavigation.selectedItemId = R.id.menu_home
            showFloatingActionButton(true)
        }


        createButton.setOnClickListener {
            showCreateArticleDialog()
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }



    private fun showFloatingActionButton(show: Boolean) {
        if (show) {
            createButton.show()
        } else {
            createButton.hide()
        }
    }

    private fun showCreateArticleDialog() {
        val dialogView = layoutInflater.inflate(R.layout.create_article, null)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear articulo")
        builder.setView(dialogView)

        val editTextArticleName = dialogView.findViewById<EditText>(R.id.editTextArticleName)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val editTextArticlePrice = dialogView.findViewById<EditText>(R.id.editTextArticlePrice)
        val increase = dialogView.findViewById<Button>(R.id.increaseButton)
        val decrease = dialogView.findViewById<Button>(R.id.decreaseButton)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.quantityTextView)

        // Initialize quantity and set the initial text in the TextView
        var quantity = 1
        quantityTextView.text = quantity.toString() // Set initial quantity to TextView

        // Set up the spinner adapter
        spinnerCategory.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.opciones_spinner,
            android.R.layout.simple_spinner_item
        )
        spinnerCategory.setSelection(0)

        // Set click listeners for the increase and decrease buttons
        increase.setOnClickListener {
            quantity++ // Increase quantity
            quantityTextView.text = quantity.toString() // Update TextView
        }
        decrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            } else {
                Toast.makeText(this, "La cantidad no puede ser menor a 1", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setPositiveButton("Crear") { dialog, _ ->
            val articleName = editTextArticleName.text.toString()
            val category = spinnerCategory.selectedItem.toString()
            val articlePrice = editTextArticlePrice.text.toString()

            if (quantity <= 0) {
                Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (articleName.isEmpty() || category.isEmpty() || articlePrice.isEmpty()) {
                Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (articlePrice.toDouble() <= 0) {
                Toast.makeText(this, "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (articleName.length > 20) {
                Toast.makeText(this, "El nombre del articulo no puede tener más de 20 caracteres", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val item = Item("", articleName, mAuth.currentUser!!.uid, quantity, category, articlePrice.toDouble(), false)


            mItemProvider.addItem(item).addOnSuccessListener {
                Toast.makeText(this, "Articulo creado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al crear el articulo", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        builder.create().show() // Show the dialog
    }



}