package com.alexpetitjean.groceries.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items")
    fun getShoppingItems(): Flowable<List<ShoppingItem>>

    // Broken: https://youtrack.jetbrains.com/issue/KT-17959
    // Kotlin doesn't preserve the parameter names
    @Query("SELECT * FROM shopping_items WHERE shopping_list_id = :arg0")
    fun getShoppingItems(shoppingListId: Int): Flowable<List<ShoppingItem>>

    @Insert
    fun insertAll(vararg shoppingItem: ShoppingItem)

    @Delete
    fun delete(shoppingItem: ShoppingItem)

    @Query("DELETE FROM shopping_items")
    fun deleteAll()
}
