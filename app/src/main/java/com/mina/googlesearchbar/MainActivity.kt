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
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    var p = Pattern.compile("[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(googleSearchBar.toolbar)

        val adapter = SearchItemsAdapter() as GoogleSearchAdapter<RecyclerView.ViewHolder?>
        googleSearchBar.setAdapter(adapter)
        googleSearchBar.onProcessSearchListener = object : OnProcessSearchListener {
            override fun searchText(newText: String) {
               if(p.matcher(newText).find()){
                   webView.loadUrl(newText)
               }else{
                   webView.loadUrl("https://www.google.com.eg/search?q=$newText")
               }

            }
        }

        val items = ArrayList<GoogleSearchBarItemModel>()
        items.add(ItemModelGoogle("Facebook.com", "d"))
        items.add(ItemModelGoogle("google.com", "d"))
        items.add(ItemModelGoogle("merna", "d"))
        items.add(ItemModelGoogle("mari", "d"))
        adapter.pushData(items)


        initWebView()
    }

    private fun initWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
    }
}
