/*
 * Created by : Mina George ,  at :   12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.util.TypedValue
import androidx.annotation.ColorRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.get
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mina.googlesearchbar.events.OnProcessSearchListener
import com.mina.googlesearchbar.events.OnSearchTextSelected
import com.mina.googlesearchbar.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
/**

 this is the main class for GoogleSearchBar library,
 contains two views searchBar view and the child view ex:(webView)

 **/

@SuppressLint("SupportAnnotationUsage")
class GoogleSearchBar : FrameLayout {


    var onProcessSearchListener: OnProcessSearchListener? = null
    private var googleSearchAdapter: GoogleSearchAdapter<RecyclerView.ViewHolder?>? = null
    private var disposable = CompositeDisposable()
    private lateinit var searchContainer: ConstraintLayout
    private lateinit var searchEditText: EditText
    private lateinit var buttonSearch: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var openSearchButton: ImageView
    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var currentText: TextView
    lateinit var toolbar: Toolbar

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        initStyle(attrs, defStyleAttr)
    }

    private fun initStyle(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoogleSearchBarView, defStyleAttr, 0)
            ?: //TODO set default configs and styles .
            return

        //TODO set searchBar configs from the typed array .

        // to be re-used by a later caller
        typedArray.recycle()
    }

    private fun init(context: Context) {
        disposable = CompositeDisposable()
        inflateView()
        initEditText()
        initRecyclerView()
        initButtons()
    }

    // initialize view open and close actions.
    private fun initButtons() {

        clearButton.setOnClickListener {
            closeSearchBar()
        }

        openSearchButton.setOnClickListener {
            openSearchBar()
        }
    }

    private fun initRecyclerView() {
        filterRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initEditText() {
        // An observable create for listening to editText changes and handle user fast writing.
        disposable.add(RxTextView.beforeTextChangeEvents(searchEditText)
            .debounce(150, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .skip(1)
            .map { it.text().toString().trim().toLowerCase() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    googleSearchAdapter?.processFilter(it)
            },
                { throwable -> Log.e(TAG, throwable.message) })
        )

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.toString().trim().length >= 2) {
                    ViewUtils.hideKeyboard(searchEditText)
                    onProcessSearchListener?.searchText(v.text.toString().trim())
                    currentText.text = v.text.toString().trim()
                    googleSearchAdapter?.processFilter("")
                    closeSearchBar()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun inflateView() {
        // inflate and initialize SearchBar view and its children.
        LayoutInflater.from(context).inflate(R.layout.google_search_layout, this, true)
        searchContainer = findViewById(R.id.searchContainer)
        searchEditText = findViewById(R.id.searchEditText)
        buttonSearch = findViewById(R.id.buttonSearch)
        clearButton = findViewById(R.id.buttonClear)
        filterRecyclerView = findViewById(R.id.filterRecyclerView)
        appBarLayout = findViewById(R.id.app_bar)
        openSearchButton = findViewById(R.id.openSearchButton)
        toolbar = findViewById(R.id.toolbar)
        currentText = findViewById(R.id.currentText)

    }

    /**

     this is a dirty implementation will be enhanced soon,
     this is responsible for handling the order of views on frameLayout
     to make the child view (ex.webView) the first and searchBar the second.

     * **/

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val searchBarView: View? = get(0)
        val contentLayout: View? = if (childCount == 2) get(1) else null

        contentLayout?.let {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            val tv = TypedValue()
            var actionBarHeight = 0
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            }
            params.setMargins(0, actionBarHeight, 0, 0)
            it.layoutParams = params
            removeView(it)
            removeView(searchBarView)
        }

        if (childCount == 0) {
            addView(contentLayout)
            addView(searchBarView)
        }


    }

    private fun closeSearchBar() {
        currentText.text = searchEditText.text
        searchContainer.animate()
            .setDuration(400)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    searchContainer.visibility = View.GONE
                }
            })
    }

    private fun openSearchBar() {
        searchEditText.setText(currentText.text)
        searchContainer.animate()
            .setDuration(400)
            .alpha(1.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    searchContainer.visibility = View.VISIBLE
                }
            })
    }


    fun setAdapter(adapter: GoogleSearchAdapter<RecyclerView.ViewHolder?>) {
        filterRecyclerView.adapter = adapter
        this.googleSearchAdapter = adapter
        this.googleSearchAdapter?.onSearchTextSelected = object : OnSearchTextSelected {
            override fun selectedText(newText: String) {
                ViewUtils.hideKeyboard(searchEditText)
                googleSearchAdapter?.processFilter("")
                closeSearchBar()
                currentText.text = newText
                onProcessSearchListener?.searchText(newText)
            }
        }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    companion object {
        const val TAG: String = "GoogleSearchBarTAG"
    }
}