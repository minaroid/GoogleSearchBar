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
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mina.googlesearchbar.events.OnProcessSearchListener
import com.mina.googlesearchbar.events.OnSearchTextSelected
import com.mina.googlesearchbar.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@SuppressLint("SupportAnnotationUsage")
class GoogleSearchBar : FrameLayout {


    var onProcessSearchListener: OnProcessSearchListener? = null
    private var googleSearchAdapter: GoogleSearchAdapter<RecyclerView.ViewHolder?>? = null

    private var attributes: TypedArray? = null
    private var searchBarMarginLeft = 0
    private var searchBarMarginRight = 0
    private var searchBarMarginBottom = 0
    private var searchBarMarginTop = 0
    private var searchBarPaddingLeft = 0
    private var searchBarPaddingRight = 0
    private var searchBarPaddingBottom = 0
    private var searchBarPaddingTop = 0

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
        attributes = context.obtainStyledAttributes(attrs, R.styleable.GoogleSearchBarView)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attributes = context.obtainStyledAttributes(attrs, R.styleable.GoogleSearchBarView)
        init(context)
    }

    private fun init(context: Context) {
        disposable = CompositeDisposable()
        inflateView()
        initEditText()
        initRecyclerView()
        initButtons()
//        // searchEditText configs
//        googleSearchBarEditText = GoogleSearchBarEditText(context)
//        googleSearchBarEditText!!.imeOptions = EditorInfo.IME_ACTION_SEARCH
//        googleSearchBarEditText!!.onTextChangedListener = object : OnTextChangedListener {
//            override fun textChanged(newText: String) {
//                googleSearchAdapter?.processFilter(newText)
//            }
//        }
//        googleSearchBarEditText!!.onSearchTextSelected = object : OnSearchTextSelected {
//            override fun selectedText(newText: String) {
//                googleSearchAdapter?.processFilter("")
//                onProcessSearchListener?.searchText(newText)
//            }
//        }
//
//        initSearchEditTextAttr()
//
//        // filterRecyclerView configs
//        googleSearchBarRecyclerView = GoogleSearchBarRecyclerView(context)
//        googleSearchBarRecyclerView?.layoutManager = LinearLayoutManager(context)
//
//        // add views to main layout container
//        orientation = VERTICAL
//        addView(googleSearchBarEditText)
//        addView(googleSearchBarRecyclerView)
    }

    private fun initButtons() {
        clearButton.setOnClickListener {
            searchEditText.setText("")
            googleSearchAdapter?.processFilter("")
        }
        openSearchButton.setOnClickListener {
            openSearchBar()
        }
    }

    private fun initRecyclerView() {
        filterRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initEditText() {
        disposable.add(RxTextView.beforeTextChangeEvents(searchEditText)
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .skip(1)
            .map { it.text().toString().trim().toLowerCase() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    googleSearchAdapter?.processFilter(it)
                    clearButton.visibility = View.VISIBLE
                } else
                    clearButton.visibility = View.GONE
            },
                { throwable -> Log.e(TAG, throwable.message) })
        )

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.toString().trim().length >= 3) {
                    ViewUtils.hideKeyboard(searchEditText)
                    googleSearchAdapter?.processFilter("")
                    onProcessSearchListener?.searchText(v.text.toString().trim())
                    currentText.text = v.text.toString().trim()
                    closeSearchBar()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun inflateView() {
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

    private fun closeSearchBar() {

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

    private fun initSearchEditTextAttr() {

//        attributes?.getResourceId(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_background,
//            R.drawable.def_google_search_bg
//        )?.let { googleSearchBarEditText!!.setBackgroundResource(it) }
//
//        attributes?.getString(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_hint
//        )?.let { googleSearchBarEditText!!.hint = it }
//
//        attributes?.getResourceId(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_text_color,
//            R.color.colorPrimaryDark
//        )?.let { googleSearchBarEditText!!.setTextColor(it) }
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_elevation,
//            0f
//        )?.let { googleSearchBarEditText?.elevation = it }
//
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_textSize,
//            0f
//        )?.let { googleSearchBarEditText?.textSize = it }
//
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_marginLeft,
//            0f
//        )?.let { searchBarMarginLeft = it.toInt() }
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_marginRight,
//            0f
//        )?.let { searchBarMarginRight = it.toInt() }
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_marginTop,
//            0f
//        )?.let { searchBarMarginTop = it.toInt() }
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_marginBottom,
//            0f
//        )?.let { searchBarMarginBottom = it.toInt() }
//
//
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_paddingBottom,
//            0f
//        )?.let { searchBarPaddingBottom = it.toInt() }
//
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_paddingTop,
//            0f
//        )?.let { searchBarPaddingTop = it.toInt() }
//
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_paddingRight,
//            0f
//        )?.let { searchBarPaddingRight = it.toInt() }
//
//        attributes?.getDimension(
//            R.styleable.GoogleSearchBarView_GoogleSB_et_paddingLeft,
//            0f
//        )?.let { searchBarPaddingLeft = it.toInt() }
//
//
//        val params = LayoutParams(
//            LayoutParams.MATCH_PARENT,
//            LayoutParams.MATCH_PARENT
//        )
//
//        params.setMargins(searchBarMarginLeft, searchBarMarginTop, searchBarMarginRight, searchBarMarginBottom)
//        googleSearchBarEditText?.layoutParams = params
//
//        googleSearchBarEditText?.setPadding(
//            searchBarPaddingLeft,
//            searchBarPaddingTop,
//            searchBarPaddingRight,
//            searchBarPaddingBottom
//        )
    }

    fun setAdapter(adapter: GoogleSearchAdapter<RecyclerView.ViewHolder?>) {
        filterRecyclerView.adapter = adapter
        this.googleSearchAdapter = adapter
        this.googleSearchAdapter?.onSearchTextSelected = object : OnSearchTextSelected {
            override fun selectedText(newText: String) {
                ViewUtils.hideKeyboard(searchEditText)
                googleSearchAdapter?.processFilter("")
                onProcessSearchListener?.searchText(newText)
                currentText.text = newText
                searchEditText.setText( newText)
                closeSearchBar()
            }
        }
    }

    fun setSearchPaddingRelative(start: Int = 0, top: Int = 0, end: Int = 0, bottom: Int = 0) {
//        val params = LayoutParams(
//            LayoutParams.MATCH_PARENT,
//            LayoutParams.MATCH_PARENT
//        ).set
//        params.setMargins(left, top, right, bottom)
//        = params
    }

    @ColorRes
    fun setSearchBackgroundColor(color: Int) {
//        googleSearchBarEditText?.setBackgroundColor(color)
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    companion object {
        const val TAG: String = "GoogleSearchBarTAG"
    }
}