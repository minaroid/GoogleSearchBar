/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchItemsAdapter : GoogleSearchAdapter<SearchItemsAdapter.ViewHolder?>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun getItemCount() = 5

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {}
}