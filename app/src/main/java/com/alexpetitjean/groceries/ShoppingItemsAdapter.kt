package com.alexpetitjean.groceries

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alexpetitjean.groceries.data.ShoppingItem
import com.alexpetitjean.myapplication.R
import kotlinx.android.synthetic.main.item_shopping_item.view.*

class ShoppingItemsAdapter : RecyclerView.Adapter<ShoppingItemsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var shoppingItems: List<ShoppingItem> = emptyList()

    override fun getItemCount(): Int = shoppingItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_shopping_item))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.shoppingItemName.text = shoppingItems[position].name
    }
}
