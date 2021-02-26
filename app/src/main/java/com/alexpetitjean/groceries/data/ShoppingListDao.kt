package com.alexpetitjean.groceries.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists")
    fun getShoppingLists(): Flowable<List<ShoppingList>>

    // Broken: https://youtrack.jetbrains.com/issue/KT-17959
    // Kotlin doesn't preserve the parameter names
//    @Query("SELECT * FROM shopping_lists WHERE id = :id LIMIT 1")
//    fun getShoppingList(id: Int): Flowable<ShoppingList>

    @Insert
    fun insertAll(vararg shoppingList: ShoppingList)

    @Delete
    fun delete(shoppingList: ShoppingList)

    @Query("DELETE FROM shopping_lists")
    fun deleteAll()
}
