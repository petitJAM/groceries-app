package com.alexpetitjean.groceries.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alexpetitjean.groceries.data.ShoppingList
import com.alexpetitjean.groceries.inflate
import com.alexpetitjean.myapplication.R
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_shopping_list.view.*

class ShoppingListsAdapter : RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var shoppingLists: List<ShoppingList> = emptyList()

    private val onClickSubject: PublishSubject<ShoppingList> = PublishSubject.create()

    fun setShoppingLists(shoppingLists: List<ShoppingList>) {
        this.shoppingLists = shoppingLists
        notifyDataSetChanged()
    }

    fun clicks(): Observable<ShoppingList> {
        return onClickSubject.subscribeOn(Schedulers.io())
    }

    override fun getItemCount(): Int = shoppingLists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_shopping_list))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val shoppingList = shoppingLists[position]
            shoppingListName.text = shoppingList.name
            shoppingListItem.setOnClickListener { onClickSubject.onNext(shoppingList) }
        }
    }
}
