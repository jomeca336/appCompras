package com.camilo.appcompras.provider

import com.camilo.appcompras.entity.Item
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ItemProvider {

    private var collection: CollectionReference = FirebaseFirestore.getInstance().collection("items")

    fun addItem(item: Item): Task<Void> {
        item.id = collection.document().id
        return collection.document(item.id).set(item)
    }

   fun getItemsByUser(uid: String): Query {
       return collection.whereEqualTo("uid", uid)
   }

   fun getItemsByCheckedAndUser(uid: String, checked: Boolean): Query {
       return collection.whereEqualTo("uid", uid).whereEqualTo("state", checked)
   }

    fun updateState(item: Item): Task<Void> {
        return collection.document(item.id).update("state", item.state)
    }

    fun deleteByChecked(): Task<QuerySnapshot> {
        return collection.whereEqualTo("state", true).get().addOnSuccessListener {
            for (document in it) {
                collection.document(document.id).delete()
            }
        }
    }

    fun getItemByCategory(category: String): Query {
        return collection.whereEqualTo("category", category)
    }

    fun deleteItem(item: Item) {
        collection.document(item.id).delete()
    }
}