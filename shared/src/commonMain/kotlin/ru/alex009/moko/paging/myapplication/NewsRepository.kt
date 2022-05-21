package ru.alex009.moko.paging.myapplication

import kotlinx.coroutines.delay

internal class NewsRepository {
    private var counter = 0

    suspend fun loadNews(): List<News> {
        delay(2000)

        if (counter == 0) {
            counter++
            throw IllegalStateException("No internet connection")
        }
        if (counter == 1) {
            counter++
            return emptyList()
        }

        counter = 0
        return List(100) { idx ->
            News(id = idx, title = "News title $idx", description = "some description of $idx")
        }
    }
}
