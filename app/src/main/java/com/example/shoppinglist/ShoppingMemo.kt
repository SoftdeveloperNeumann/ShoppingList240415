package com.example.shoppinglist

data class ShoppingMemo(
    var quantity: Int,
    var product: String,
    var id: Long
) {

    override fun toString(): String {
        return "$quantity x $product"
    }
}