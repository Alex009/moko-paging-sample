package ru.alex009.moko.paging.myapplication

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.asState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.dataTransform
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch

class NewsViewModel internal constructor(
    private val repository: NewsRepository
) : ViewModel() {
    // can't use default arg with iOS
    constructor() : this(repository = NewsRepository())

    private val _state: MutableLiveData<ResourceState<List<News>, Throwable>> =
        MutableLiveData(ResourceState.Loading())

    val state: LiveData<ResourceState<List<UnitItem>, StringDesc>> = _state
        .dataTransform {
            map { list ->
                list.map<News, UnitItem> { item ->
                    UnitItem.NewsItem(
                        id = item.id,
                        title = item.title,
                        caption = item.description
                    )
                }
            }
        }
        .errorTransform {
            map { it.message.orEmpty().desc() }
        }

    fun start() = apply { loadData() }

    fun onRetryPressed() = loadData()

    fun onRefreshPressed() = loadData()

    private fun loadData() {
        viewModelScope.launch {
            _state.value = ResourceState.Loading()
            try {
                val items: List<News> = repository.loadNews()
                _state.value = items.asState()
            } catch (exc: Exception) {
                _state.value = ResourceState.Failed(exc)
            }
        }
    }

    sealed interface UnitItem {
        data class NewsItem(
            val id: Int,
            val title: String,
            val caption: String
        ) : UnitItem
    }
}
