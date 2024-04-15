package com.example.shoppinglist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ShoppingMemoDbHelper(context:Context) : SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION ) {

    // Sinnvolle Konstanten für eine bessere Verwendung der Datenbank und des Helpers
    companion object{
        private const val DB_NAME = "shopping_list.db"
        private const val DB_VERSION = 1
        private const val TAG = "ShoppingMemoDbHelper"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}