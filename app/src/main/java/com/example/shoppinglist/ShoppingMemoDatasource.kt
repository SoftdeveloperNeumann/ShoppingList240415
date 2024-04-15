package com.example.shoppinglist

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ShoppingMemoDatasource(context: Context) {

    private var db: SQLiteDatabase? = null
    private val helper: ShoppingMemoDbHelper

    private val TAG = "ShoppingMemoDatasource"

    private val columns = arrayOf(
        ShoppingMemoDbHelper.COLUMN_ID,
        ShoppingMemoDbHelper.COLUMN_QUANTITY,
        ShoppingMemoDbHelper.COLUMN_PRODUCT
    )

    val allShoppingMemos: List<ShoppingMemo>
        get() {
            val shoppingMemoList: MutableList<ShoppingMemo> = ArrayList()

            val cursor = db?.query(
                ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
                columns,
                null, null, null, null, null
            )

            cursor?.moveToFirst()
            var memo: ShoppingMemo
            while (!cursor?.isAfterLast!!) {
                memo = cursorToShoppingMemo(cursor)
                shoppingMemoList.add(memo)
                cursor.moveToNext()
            }
            cursor.close()

            return shoppingMemoList
        }

    private fun cursorToShoppingMemo(cursor: Cursor): ShoppingMemo {
        val idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID)
        val quantityIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_QUANTITY)
        val productIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PRODUCT)

        val id = cursor.getLong(idIndex)
        val quantity = cursor.getInt(quantityIndex)
        val product = cursor.getString(productIndex)

        return ShoppingMemo(quantity,product,id)
    }

    init {
        helper = ShoppingMemoDbHelper(context)
        Log.d(TAG, "DB-Init: Datasource hat den Helper angelegt")
    }

    fun open() {
        db = helper.writableDatabase
    }

    fun close() {
        helper.close()
    }
}