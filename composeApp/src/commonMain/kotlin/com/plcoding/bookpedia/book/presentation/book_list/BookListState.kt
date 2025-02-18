package com.plcoding.bookpedia.book.presentation.book_list
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

// MVI Model view intent
// Intent is the actions the user can do from user interation to send to the view model.
data class BookListState (
    val searchQuery: String = "Metagrappler",
    val searchResults: List<Book> = emptyList(),
    val favouriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null,
)
