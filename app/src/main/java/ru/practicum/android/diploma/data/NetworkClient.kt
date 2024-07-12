package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.network.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
