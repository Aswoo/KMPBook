package com.sdu.kmpbook.book.presentation.book_list

import com.sdu.kmpbook.book.domain.Book
import com.sdu.kmpbook.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)