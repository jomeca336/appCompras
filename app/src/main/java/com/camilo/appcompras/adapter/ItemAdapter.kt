package com.camilo.appcompras.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.appcompras.R
import com.camilo.appcompras.entity.Item
import com.camilo.appcompras.fragment.ShoppingFragment
import com.camilo.appcompras.provider.ItemProvider
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ItemAdapter(
    options: FirestoreRecyclerOptions<Item>,
    val context: Context,
    var listener: PriceListener
):
    FirestoreRecyclerAdapter<Item, ItemAdapter.ViewHolder>(options) {

    val mItemProvider = ItemProvider()

    private var totalPrice = 0.0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val itemNameTextView: TextView = view.findViewById(R.id.itemName)
        val itemQuantityTextView: TextView = view.findViewById(R.id.itemQuantity)
        val itemPriceTextView: TextView = view.findViewById(R.id.itemPrice)
        val itemCheckBox: CheckBox = view.findViewById(R.id.itemCheckBox)
        val itemCategoryTextView: TextView = view.findViewById(R.id.itemCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Item) {
        holder.itemNameTextView.text = model.name
        holder.itemQuantityTextView.text = model.quantity.toString()
        holder.itemPriceTextView.text = model.price.toString()
        holder.itemCategoryTextView.text = model.category
        holder.itemCheckBox.isChecked = model.state

        val price = model.price * model.quantity
        totalPrice += price
        listener.onPriceChanged(totalPrice)

        holder.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
           model.state = isChecked
            mItemProvider.updateState(model).addOnSuccessListener {
                notifyItemChanged(position)
            }
        }
    }






}