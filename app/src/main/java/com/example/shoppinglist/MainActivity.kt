package com.example.shoppinglist

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.databinding.DialogEditShoppingmemoBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: DialogEditShoppingmemoBinding
    private lateinit var datasource: ShoppingMemoDatasource
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dialogBinding = DialogEditShoppingmemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datasource = ShoppingMemoDatasource(this)
        initShoppingMemoListView()
        activateAddButton()
        initContextualActionBar()
    }


    private fun initShoppingMemoListView() {
        val emptyListForInit: List<ShoppingMemo> = ArrayList()
        val adapter = object : ArrayAdapter<ShoppingMemo>(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            emptyListForInit
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                val memo = binding.lvShoppingMemos.getItemAtPosition(position) as ShoppingMemo

                if (memo.isSelected) {
                    view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    view.setTextColor(Color.rgb(175, 175, 175))
                } else {
                    view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    view.setTextColor(Color.DKGRAY)
                }

                return view
            }
        }

        binding.lvShoppingMemos.adapter = adapter

        binding.lvShoppingMemos.setOnItemClickListener { parent, view, position, id ->
            val memo = parent.getItemAtPosition(position) as ShoppingMemo
            datasource.updateShoppingMemo(memo.quantity, memo.product, memo.id, !memo.isSelected)
            showAllShoppingMemos()
        }
    }

    private fun activateAddButton() {
        binding.btnAddProduct.setOnClickListener {
            val quantityString = binding.etQuantity.text.toString()
            val product = binding.etProduct.text.toString()

            if (quantityString.isBlank()) {
                binding.etQuantity.error = "Anzahl darf nicht leer sein"
                binding.etQuantity.requestFocus()
                return@setOnClickListener
            }

            if (product.isBlank()) {
                binding.etProduct.error = "Artikel darf nicht leer sein"
                binding.etProduct.requestFocus()
                return@setOnClickListener
            }

            val quantity = quantityString.toInt()
            binding.etQuantity.text.clear()
            binding.etProduct.text.clear()
            binding.etQuantity.requestFocus()
            datasource.createShoppingMemo(quantity, product)
            showAllShoppingMemos()

        }
    }

    private fun initContextualActionBar() {
        binding.lvShoppingMemos.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        binding.lvShoppingMemos.setMultiChoiceModeListener(object : MultiChoiceModeListener {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.menu_contextual, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                val editItem = menu?.findItem(R.id.action_edit)
                editItem?.isVisible = binding.lvShoppingMemos.checkedItemCount == 1
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                val touchedModePositions = binding.lvShoppingMemos.checkedItemPositions
                when (item?.itemId) {
                    R.id.action_delete -> {
                        touchedModePositions.forEach { key, value ->
                            if (value) {
                                val memo =
                                    binding.lvShoppingMemos.getItemAtPosition(key) as ShoppingMemo
                                datasource.deleteShoppingMemo(memo)
                            }
                        }
                    }

                    R.id.action_edit -> {
                        touchedModePositions.forEach { key, value ->
                            if(value){
                                val memo = binding.lvShoppingMemos.getItemAtPosition(key) as ShoppingMemo

                            }
                        }
                    }
                }
                showAllShoppingMemos()
                mode?.finish()
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

            override fun onItemCheckedStateChanged(
                mode: ActionMode?,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                mode?.invalidate()
            }
        })
    }

    private fun createShoppingMemoDialog(memo: ShoppingMemo): AlertDialog?{

        return null
    }


    private fun showAllShoppingMemos() {
        val list = datasource.allShoppingMemos
        val adapter = binding.lvShoppingMemos.adapter as ArrayAdapter<ShoppingMemo>
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: DataSource wird geöffnet")
        datasource.open()
        showAllShoppingMemos()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Datasouce wird geschlossen")
        datasource.close()
    }
}