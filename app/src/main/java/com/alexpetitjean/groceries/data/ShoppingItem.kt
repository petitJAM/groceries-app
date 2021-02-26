package com.alexpetitjean.groceries.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "shopping_items",
        foreignKeys = arrayOf(ForeignKey(entity = ShoppingList::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("shopping_list_id"),
                onDelete = ForeignKey.CASCADE)))
data class ShoppingItem(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                        var name: String = "",
                        @ColumnInfo(name = "shopping_list_id")
                        var shoppingListId: Int = 0)
