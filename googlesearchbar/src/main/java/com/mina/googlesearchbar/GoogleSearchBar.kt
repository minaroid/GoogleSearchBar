/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GoogleSearchBar : LinearLayout {

    private lateinit var googleSearchBarEditText: GoogleSearchBarEditText
    private lateinit var googleSearchBarRecyclerView: GoogleSearchBarRecyclerView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = VERTICAL
        googleSearchBarEditText = GoogleSearchBarEditText(context)
        googleSearchBarRecyclerView = GoogleSearchBarRecyclerView(context)
        googleSearchBarRecyclerView.layoutManager = LinearLayoutManager(context)

        addView(googleSearchBarEditText)
        addView(googleSearchBarRecyclerView)
    }

    public fun setAdapter(adapter: GoogleSearchAdapter<RecyclerView.ViewHolder?>) {
        googleSearchBarRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG: String = "GoogleSearchBarTAG"
    }
}