package com.sdu.kmpbook.book.data.repository

import androidx.sqlite.SQLiteException
import com.sdu.kmpbook.book.data.database.FavoriteBookDao
import com.sdu.kmpbook.book.data.mappers.toBook
import com.sdu.kmpbook.book.data.mappers.toBookEntity
import com.sdu.kmpbook.book.data.network.RemoteBookDataSource
import com.sdu.kmpbook.book.domain.Book
import com.sdu.kmpbook.book.domain.BookRepository
import com.sdu.kmpbook.core.domain.DataError
import com.sdu.kmpbook.core.domain.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.sdu.kmpbook.core.domain.Result
import com.sdu.kmpbook.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if(localResult == null) {
            remoteBookDataSource
                .getBookDetails(bookId)
                .map { it.description }
        } else {
            Result.Success(localResult.description)
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch(e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }
}