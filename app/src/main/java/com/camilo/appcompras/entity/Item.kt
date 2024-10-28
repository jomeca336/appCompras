package com.camilo.appcompras.entity

data class Item (
    var id: String = "",
    var name: String = "",
    var uid: String = "",
    var quantity: Int = 0,
    var category: String = "",
    var price: Double = 0.0,
    var state: Boolean = false
)