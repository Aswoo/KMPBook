package com.sdu.kmpbook.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.bookpedia.core.data.HttpClientFactory
import com.sdu.kmpbook.book.data.database.DatabaseFactory
import com.sdu.kmpbook.book.data.database.FavoriteBookDatabase
import com.sdu.kmpbook.book.data.network.RemoteBookDataSource
import com.sdu.kmpbook.book.domain.BookRepository
import com.sdu.kmpbook.book.data.network.KtorRemoteBookDataSource
import com.sdu.kmpbook.book.data.repository.DefaultBookRepository
import com.sdu.kmpbook.book.presentation.SelectedBookViewModel
import com.sdu.kmpbook.book.presentation.book_list.BookListViewModel
import com.sdu.kmpbook.book.presentation.book_detail.BookDetailViewModel

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}