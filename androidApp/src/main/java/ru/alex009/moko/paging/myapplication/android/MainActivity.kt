package ru.alex009.moko.paging.myapplication.android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.getViewModel
import dev.icerock.moko.mvvm.utils.bindNotNull
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import ru.alex009.moko.paging.myapplication.NewsViewModel
import ru.alex009.moko.paging.myapplication.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: NewsViewModel = getViewModel { NewsViewModel().start() }

        val adapter = UnitsRecyclerViewAdapter(this)
        with(binding.newsRecyclerView) {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.state.bindNotNull(this) { state ->
            bindState(binding, adapter, viewModel, state)
        }
    }

    private fun bindState(
        binding: ActivityMainBinding,
        adapter: UnitsRecyclerViewAdapter,
        viewModel: NewsViewModel,
        state: ResourceState<List<NewsViewModel.UnitItem>, StringDesc>
    ) {
        adapter.units = state.dataValue()?.map { it.toUnit() } ?: emptyList()
        binding.newsRecyclerView.visibility = if (state.isSuccess()) View.VISIBLE else View.GONE

        binding.loadingProgressBar.visibility = if (state.isLoading()) View.VISIBLE else View.GONE

        bindMessageState(binding, state, viewModel)
    }

    private fun bindMessageState(
        binding: ActivityMainBinding,
        state: ResourceState<List<NewsViewModel.UnitItem>, StringDesc>,
        viewModel: NewsViewModel
    ) {
        listOf(binding.centerButton, binding.centerTitleText, binding.centerButton).forEach {
            it.visibility = when (state) {
                is ResourceState.Empty, is ResourceState.Failed -> View.VISIBLE
                else -> View.GONE
            }
        }

        binding.centerTitleText.text = when (state) {
            is ResourceState.Empty -> getText(R.string.empty_title)
            is ResourceState.Failed -> getText(R.string.error_title)
            else -> ""
        }

        binding.centerCaptionText.text = when (state) {
            is ResourceState.Empty -> getText(R.string.no_news_caption)
            is ResourceState.Failed -> state.error.toString(this)
            else -> ""
        }

        binding.centerButton.text = when (state) {
            is ResourceState.Empty -> getText(R.string.refresh_btn)
            is ResourceState.Failed -> getText(R.string.retry_btn)
            else -> ""
        }
        val clickListener: View.OnClickListener? = when (state) {
            is ResourceState.Empty -> View.OnClickListener { viewModel.onRefreshPressed() }
            is ResourceState.Failed -> View.OnClickListener { viewModel.onRetryPressed() }
            else -> null
        }
        binding.centerButton.setOnClickListener(clickListener)
    }

    private fun NewsViewModel.UnitItem.toUnit(): UnitItem {
        return when (this) {
            is NewsViewModel.UnitItem.NewsItem -> NewsUnitItem(this)
        }
    }
}
