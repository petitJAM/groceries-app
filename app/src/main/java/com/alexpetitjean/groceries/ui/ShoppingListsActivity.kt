package com.alexpetitjean.groceries.ui

import android.arch.lifecycle.LifecycleActivity
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alexpetitjean.groceries.data.GroceriesDatabase
import com.alexpetitjean.groceries.data.ShoppingList
import com.alexpetitjean.groceries.toast
import com.alexpetitjean.myapplication.R
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_shopping_lists.*

class ShoppingListsActivity : LifecycleActivity() {

    val db: GroceriesDatabase by lazy {
        Room.databaseBuilder(applicationContext, GroceriesDatabase::class.java, "groceries.db").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_lists)

        val shoppingListDao = db.shoppingListDao()

        RxView.clicks(addShoppingListButton)
                .bindToLifecycle(this)
                .map { shoppingListNameInput.text.toString() }
                .doOnNext { shoppingListNameInput.text = null }
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onNext = { name ->
                            if (name.isNotEmpty()) {
                                val newList = ShoppingList(name = name)
                                shoppingListDao.insertAll(newList)
                            }
                        })

        shoppingListsRecycler.layoutManager = LinearLayoutManager(this)
        val shoppingListsAdapter = ShoppingListsAdapter()
        shoppingListsRecycler.adapter = shoppingListsAdapter

        shoppingListsAdapter.clicks()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { shoppingList ->
                            toast(shoppingList.name)
                        })

        shoppingListDao.getShoppingLists()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { shoppingListsAdapter.setShoppingLists(it) })
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}
