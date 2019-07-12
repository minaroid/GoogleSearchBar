/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mina.googlesearchbar.events.OnProcessSearchListener
import com.mina.googlesearchbar.models.GoogleSearchBarItemModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(googleSearchBar.toolbar)
        val adapter = SearchItemsAdapter() as GoogleSearchAdapter<RecyclerView.ViewHolder?>
        googleSearchBar.setAdapter(adapter)
        googleSearchBar.onProcessSearchListener = object : OnProcessSearchListener {
            override fun searchText(newText: String) {
                Toast.makeText(this@MainActivity, newText, Toast.LENGTH_SHORT).show()
            }
        }

        val items = ArrayList<GoogleSearchBarItemModel>()
        items.add(ItemModelGoogle("Facebook.com", "d"))
        items.add(ItemModelGoogle("google.com", "d"))
        items.add(ItemModelGoogle("merna", "d"))
        items.add(ItemModelGoogle("mari", "d"))
        adapter.pushData(items)
    }
}
