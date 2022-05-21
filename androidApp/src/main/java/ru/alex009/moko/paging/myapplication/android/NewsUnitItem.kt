package ru.alex009.moko.paging.myapplication.android

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import dev.icerock.moko.units.viewbinding.VBTableUnitItem
import dev.icerock.moko.units.viewbinding.VBViewHolder
import ru.alex009.moko.paging.myapplication.NewsViewModel
import ru.alex009.moko.paging.myapplication.android.databinding.NewsUnitBinding

class NewsUnitItem(
    private val newsItem: NewsViewModel.UnitItem.NewsItem
) : VBTableUnitItem<NewsUnitBinding>() {
    override val layoutId: Int = R.layout.news_unit

    override fun bindView(view: View): NewsUnitBinding = NewsUnitBinding.bind(view)

    override fun NewsUnitBinding.bindData(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        viewHolder: VBViewHolder<NewsUnitBinding>
    ) {
        titleText.text = newsItem.title
        captionText.text = newsItem.caption
    }

    override val itemId: Long = newsItem.id.toLong()
}