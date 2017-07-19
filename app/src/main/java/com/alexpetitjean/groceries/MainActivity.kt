package com.alexpetitjean.groceries

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.alexpetitjean.groceries.data.GroceriesDatabase
import com.alexpetitjean.groceries.data.ShoppingItem
import com.alexpetitjean.myapplication.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val db: GroceriesDatabase by lazy {
        Room.databaseBuilder(applicationContext, GroceriesDatabase::class.java, "groceries.db").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shoppingItemDao = db.shoppingItemDao()

        RxView.clicks(addShoppingItemButton)
                .map { shoppingItemNameInput.text.toString() }
                .doOnNext { shoppingItemNameInput.text = null }
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onNext = { name ->
                            if (name.isNotEmpty()) {
                                val newItem = ShoppingItem(name = name)
                                shoppingItemDao.insertAll(newItem)
                            }
                        })

        shoppingItemsRecycler.layoutManager = LinearLayoutManager(this)
        val shoppingItemsAdapter = ShoppingItemsAdapter()
        shoppingItemsRecycler.adapter = shoppingItemsAdapter

        shoppingItemDao.getShoppingItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { shoppingItems ->
                            shoppingItemsAdapter.shoppingItems = shoppingItems
                            shoppingItemsAdapter.notifyDataSetChanged()
                        },
                        onError = {
                            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}
