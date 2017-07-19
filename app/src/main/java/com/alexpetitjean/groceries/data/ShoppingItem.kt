package com.alexpetitjean.groceries.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                        var name: String = "")
