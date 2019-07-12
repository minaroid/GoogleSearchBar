/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class GoogleSearchBarEditText : AppCompatEditText {

    private var disposable = CompositeDisposable()

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
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        initEditTextObserver()
    }

    private fun initEditTextObserver() {
        disposable.add(RxTextView.beforeTextChangeEvents(this)
            .debounce(400, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .skip(1)
            .map { it.text().toString().trim() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ text -> Log.d(TAG, text) },
                { throwable -> Log.e(TAG, throwable.message) })
        )
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    companion object {
        const val TAG: String = "GoogleSearchBarTAG"
    }
}