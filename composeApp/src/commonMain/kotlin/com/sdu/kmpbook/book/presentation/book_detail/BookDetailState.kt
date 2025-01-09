package com.sdu.kmpbook.book.presentation.book_detail

import com.sdu.kmpbook.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
