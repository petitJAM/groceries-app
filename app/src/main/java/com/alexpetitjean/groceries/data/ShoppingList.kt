package com.alexpetitjean.groceries.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingList(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                        var name: String = "",
                        @Ignore var shoppingItems: List<ShoppingItem> = emptyList())
