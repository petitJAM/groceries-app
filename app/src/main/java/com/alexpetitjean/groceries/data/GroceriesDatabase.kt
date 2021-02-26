package com.alexpetitjean.groceries.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(ShoppingItem::class, ShoppingList::class), version = 1)
abstract class GroceriesDatabase : RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao

    abstract fun shoppingListDao(): ShoppingListDao
}
