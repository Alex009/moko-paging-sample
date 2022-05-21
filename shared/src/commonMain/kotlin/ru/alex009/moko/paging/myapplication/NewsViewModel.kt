package ru.alex009.moko.paging.myapplication

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.dataTransform
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mediatorOf
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.paging.LambdaPagedListDataSource
import dev.icerock.moko.paging.Pagination
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch

class NewsViewModel internal constructor(
    private val repository: NewsRepository
) : ViewModel() {
    // can't use default arg with iOS
    constructor() : this(repository = NewsRepository())

    private val pagination: Pagination<News> = Pagination(
        parentScope = viewModelScope,
        dataSource = LambdaPagedListDataSource { currentList ->
            val pageSize = 20
            val page = (currentList?.size ?: 0) / pageSize
            repository.loadNews(page = page, size = pageSize)
        },
        comparator = { a, b -> a.id.compareTo(b.id) },
        nextPageListener = { result: Result<List<News>> ->
            if (result.isSuccess) {
                println("Next page successful loaded")
            } else {
                println("Next page loading failed")
            }
        },
        refreshListener = { result: Result<List<News>> ->
            if (result.isSuccess) {
                println("Refresh successful")
            } else {
                println("Refresh failed")
            }
        },
        initValue = emptyList()
    )

    val state: LiveData<ResourceState<List<UnitItem>, StringDesc>> = pagination.state
        .dataTransform {
            mediatorOf(this, pagination.nextPageLoading) { list, isNextPageLoad ->
                val units: List<UnitItem> = list.map<News, UnitItem> { item ->
                    UnitItem.NewsItem(
                        id = item.id,
                        title = item.title,
                        caption = item.description
                    )
                }

                if (isNextPageLoad) units + UnitItem.LoaderItem
                else units
            }
        }
        .errorTransform {
            map { it.message.orEmpty().desc() }
        }

    fun start() = apply { loadData() }

    fun onRetryPressed() = loadData()

    fun onRefreshPressed() = loadData()

    fun onReachEndOfList() = pagination.loadNextPage()

    fun onRefresh(completion: () -> Unit) {
        viewModelScope.launch {
            pagination.refreshSuspend()
            completion()
        }
    }

    private fun loadData() {
        pagination.loadFirstPage()
    }

    sealed interface UnitItem {
        data class NewsItem(
            val id: Int,
            val title: String,
            val caption: String
        ) : UnitItem

        object LoaderItem : UnitItem
    }
}
