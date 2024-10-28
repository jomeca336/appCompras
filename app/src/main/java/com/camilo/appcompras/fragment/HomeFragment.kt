package com.camilo.appcompras.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment: Fragment(), PriceListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mItemProvider: ItemProvider
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance()
        mItemProvider = ItemProvider()
        items = mutableListOf()

        val linearLayot= view.findViewById<LinearLayout>(R.id.homeLinear)

        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        itemRecyclerView = view.findViewById(R.id.itemRecyclerView)
        itemRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val query = mItemProvider.getItemsByUser(mAuth.currentUser!!.uid)
        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()
        itemAdapter = ItemAdapter(options, this.requireContext(), this)
        itemRecyclerView.adapter = itemAdapter

        query.get().addOnSuccessListener {
            val itemCount = it.size()
            if (itemCount == 0) {
                view.findViewById<TextView>(R.id.noItemsTextView).visibility = View.VISIBLE
            } else {
                view.findViewById<TextView>(R.id.noItemsTextView).visibility = View.GONE
            }
            progressBar.visibility = View.GONE
            linearLayot.visibility= View.VISIBLE
        }.addOnFailureListener {
            Toast.makeText(this.context, "Error al obtener los items", Toast.LENGTH_SHORT).show()
        }



        return view
    }
    override fun onStart() {
        super.onStart()
        itemAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        itemAdapter.stopListening()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        itemRecyclerView.adapter = null
    }

    override fun onPriceChanged(price: Double) {
        Log.d("HomeFragment", "onPriceChanged: $price")
    }
}
