package com.sdu.kmpbook.book.data.network

import com.sdu.kmpbook.book.data.dto.BookWorkDto
import com.sdu.kmpbook.book.data.dto.SearchResponseDto
import com.sdu.kmpbook.core.domain.DataError
import com.sdu.kmpbook.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>
}