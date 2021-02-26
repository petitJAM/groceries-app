package com.alexpetitjean.groceries.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleActivity
import android.arch.persistence.room.Room
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import com.alexpetitjean.groceries.data.GroceriesDatabase
import com.alexpetitjean.groceries.data.ShoppingList
import com.alexpetitjean.groceries.toast
import com.alexpetitjean.myapplication.R
import com.jakewharton.rxbinding2.view.RxView
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.MaterialIcons
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

        drawer {
            accountHeader {
                background = R.color.accent
                onlyMainProfileImageVisible = true

                profile("George", "george@george.com") {
                    iconDrawable = IconDrawable(this@ShoppingListsActivity, MaterialIcons.md_person)
                }
                profile("Alabaster", "alabaster@dumbnames.com") {
                    iconDrawable = IconDrawable(this@ShoppingListsActivity, MaterialIcons.md_person_outline)
                }
                profile("Winchester", "winchester@dumbnames.com") {
                    iconDrawable = IconDrawable(this@ShoppingListsActivity, MaterialIcons.md_person_pin)
                }

                onProfileChanged { _, profile, current ->
                    if (current) {
                        toast("No switch")
                    } else {
                        toast("Switching to ${profile.name}")
                    }
                    true
                }
            }
            headerViewRes = R.layout.material_drawer_header
            primaryItem("Lists") {}
            primaryItem("Items") {}
            divider {}
            footer {
                secondaryItem("Settings") {}
            }

            onItemClick { view, position, drawerItem ->
                toast("$position $drawerItem")
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()

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
                            } else {
                                startLeak()
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

    override fun onPause() {
        super.onPause()
        db.close()
    }

    @SuppressLint("StaticFieldLeak")
    private fun startLeak() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                SystemClock.sleep(20000)
                return null
            }
        }.execute()
    }
}
