package com.camilo.appcompras.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.appcompras.R
import com.camilo.appcompras.adapter.ItemAdapter
import com.camilo.appcompras.adapter.PriceListener
import com.camilo.appcompras.entity.Item
import com.camilo.appcompras.provider.ItemProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class ShoppingFragment: Fragment(), PriceListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mItemProvider: ItemProvider
    private lateinit var mItemAdapter: ItemAdapter
    private lateinit var mItemRecyclerView: RecyclerView
    private lateinit var mItems: MutableList<Item>
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_shopping, container, false)

        mAuth = FirebaseAuth.getInstance()
        mItemProvider = ItemProvider()
        mItems = mutableListOf()

        mItemRecyclerView = view.findViewById(R.id.shoppingRecyclerView)
        mItemRecyclerView.layoutManager = LinearLayoutManager(this.context)
        progressBar = view.findViewById(R.id.progressBar)
        val linearLayout= view.findViewById<LinearLayout>(R.id.shoppingLinear)

        val query = mItemProvider.getItemsByCheckedAndUser(mAuth.currentUser!!.uid, true)
        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()

        mItemAdapter = ItemAdapter(options, this.requireContext(), this)
        mItemRecyclerView.adapter = mItemAdapter

        query.get().addOnSuccessListener {
            val itemCount = it.size()

            if (itemCount == 0) {
                view.findViewById<TextView>(R.id.noItemsTextView).visibility = View.VISIBLE
            }else{

                view.findViewById<TextView>(R.id.noItemsTextView).visibility = View.GONE
            }

            progressBar.visibility = View.GONE
            linearLayout.visibility= View.VISIBLE
        }.addOnFailureListener {
            Toast.makeText(this.context, "Error al obtener los items", Toast.LENGTH_SHORT).show()
        }



        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            mItemProvider.deleteByChecked().addOnSuccessListener {
                Toast.makeText(this.context, "Articulos eliminados", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this.context, "Error al eliminar los articulos", Toast.LENGTH_SHORT).show()
            }

        }


        return view
    }

    override fun onStart() {
        super.onStart()
        mItemAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mItemAdapter.stopListening()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mItemRecyclerView.adapter = null
    }

    override fun onPriceChanged(price: Double) {
        view?.findViewById<TextView>(R.id.totalTextView)?.text = price.toString()
    }
}