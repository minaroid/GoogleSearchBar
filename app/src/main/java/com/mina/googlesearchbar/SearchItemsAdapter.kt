/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_filter.view.*

class SearchItemsAdapter : GoogleSearchAdapter<SearchItemsAdapter.ViewHolder?>() {


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        holder?.bind(visibleItems[position] as ItemModelGoogle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false))
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

        fun bind(googleSearchBarItemModel: ItemModelGoogle) {
            item.searchTextView.text = googleSearchBarItemModel.text
        }

    }
}