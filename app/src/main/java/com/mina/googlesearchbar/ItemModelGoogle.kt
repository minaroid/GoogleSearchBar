/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar

import com.mina.googlesearchbar.models.GoogleSearchBarItemModel

data class ItemModelGoogle(val text: String, val imageUrl: String) : GoogleSearchBarItemModel(text)