/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import androidx.recyclerview.widget.RecyclerView
import com.mina.googlesearchbar.events.OnSearchTextSelected
import com.mina.googlesearchbar.models.GoogleSearchBarItemModel

abstract class GoogleSearchAdapter<T : RecyclerView.ViewHolder?> : RecyclerView.Adapter<T>() {

    val visibleItems = ArrayList<GoogleSearchBarItemModel>()
    val itemsTemp = ArrayList<GoogleSearchBarItemModel>()
    var onSearchTextSelected: OnSearchTextSelected? = null
    override fun getItemCount() = visibleItems.size

    override fun onBindViewHolder(holder: T, position: Int) {
        holder?.itemView?.setOnClickListener {
            onSearchTextSelected?.selectedText(visibleItems[position].searchText) }
    }

    fun pushData(items: ArrayList<GoogleSearchBarItemModel>) {
        this.itemsTemp.clear()
        this.itemsTemp.addAll(items)
        notifyDataSetChanged()
    }

    fun processFilter(newText: String) {

        if (newText.isEmpty()) {
            visibleItems.clear()
        } else {
            visibleItems.clear()
            for (item: GoogleSearchBarItemModel in itemsTemp) {
                if (item.searchText.toLowerCase().contains(newText))
                    visibleItems.add(item)
            }
        }
        notifyDataSetChanged()
    }

}