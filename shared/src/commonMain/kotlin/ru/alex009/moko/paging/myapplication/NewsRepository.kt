package ru.alex009.moko.paging.myapplication

import kotlinx.coroutines.delay

internal class NewsRepository {
    private var counter = 0

    suspend fun loadNews(page: Int, size: Int): List<News> {
        delay(2000)

        if (page != 0) {
            val offset = (page * size)
            return List(size) { idxOrig ->
                val idx = idxOrig + offset
                News(id = idx, title = "News title $idx", description = "some description of $idx")
            }
        }

        if (counter == 0) {
            counter++
            throw IllegalStateException("No internet connection")
        }
        if (counter == 1) {
            counter++
            return emptyList()
        }

        counter = 0
        return List(size) { idx ->
            News(id = idx, title = "News title $idx", description = "some description of $idx")
        }
    }
}
