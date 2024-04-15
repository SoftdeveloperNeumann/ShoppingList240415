package com.example.shoppinglist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ShoppingMemoDatasource(context: Context) {

    private var db : SQLiteDatabase? = null
    private val helper: ShoppingMemoDbHelper

    private val TAG = "ShoppingMemoDatasource"

    init {
        helper = ShoppingMemoDbHelper(context)
        Log.d(TAG, "DB-Init: Datasource hat den Helper angelegt")
    }

    fun open(){
        db = helper.writableDatabase
    }

    fun close(){
        helper.close()
    }
}