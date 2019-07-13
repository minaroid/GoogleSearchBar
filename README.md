# GoogleSearchBar

A simple SearchBar for Android

* API 21+


<img alt="Card sample" width="360" height="600" src="screenshots/SVID_20.gif" />


## Download

Add the JitPack repository to the build.gradle file:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the Gradle dependency:
```groovy
	        implementation 'com.github.minageorge5080:GoogleSearchBar:1.0.0'
```


## Usage
Add SimpleSearchView to your AppBarLayout:

```xml
 <com.mina.googlesearchbar.GoogleSearchBar
            android:id="@+id/googleSearchBar"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent">

        <WebView
                android:id="@+id/webView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>

  </com.mina.googlesearchbar.GoogleSearchBar>


```

### Setup in activity
```java
val adapter = SearchItemsAdapter() as GoogleSearchAdapter<RecyclerView.ViewHolder?>
        googleSearchBar.setAdapter(adapter)
        googleSearchBar.onProcessSearchListener = object : OnProcessSearchListener {
            override fun searchText(newText: String) {
                if (p.matcher(newText).find()) {
                    webView.loadUrl(newText)
                } else {
                    webView.loadUrl("https://www.google.com.eg/search?q=$newText")
                }

            }
        }

        val items = ArrayList<GoogleSearchBarItemModel>()
        items.add(ItemModelGoogle("https://www.facebook.com", ""))
        items.add(ItemModelGoogle("https://www.google.com", ""))
        items.add(ItemModelGoogle("https://www.youtube.com", ""))
        items.add(ItemModelGoogle("https://www.instgram.com", ""))
        items.add(ItemModelGoogle("https://www.linkedin.com", ""))
        items.add(ItemModelGoogle("https://www.twitter.com", ""))
        adapter.pushData(items)

```
### Setup autoComplete adapter
```java
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
```

### Adapter item model
```java
data class ItemModelGoogle(val text: String, val imageUrl: String) : GoogleSearchBarItemModel(text)
```

## License
    Copyright (C) 2019 Mina George

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
