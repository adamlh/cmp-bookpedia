package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import com.plcoding.bookpedia.book.domain.Book

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.no_favorites
import cmp_bookpedia.composeapp.generated.resources.no_search_results
import cmp_bookpedia.composeapp.generated.resources.search_results
import cmp_bookpedia.composeapp.generated.resources.syllabus
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookListScreenRoot (
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is BookListAction.OnBookClick -> onBookClick(action.book)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}



// Easier to test in isolated UI test as just need to provide instance.
// Dont rely on view model reference.
@Composable
fun BookListScreen (
    state: BookListState,
    onAction: (BookListAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState { 3}
    val searchResultsListState = rememberLazyListState()
    val favouriteBookListState = rememberLazyListState()
    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BookListAction.OnSearchQueryChange(it))
            },
            onImgSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)

        )
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            color = SandYellow,
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[state.selectedTabIndex])
                        )

                    }
                ) {
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(0))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier
                                .padding(vertical = 12.dp)

                        )
                    }

                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites),
                            modifier = Modifier
                                .padding(vertical = 12.dp)

                        )
                    }

                    Tab(
                        selected = state.selectedTabIndex == 2,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(2))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.syllabus),
                            modifier = Modifier
                                .padding(vertical = 12.dp),

                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { pageIndex ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                    when(pageIndex) {
                        0 -> {
                            if (state.isLoading) {
                                CircularProgressIndicator()
                            } else {
                                when {
                                    state.errorMessage != null -> {
                                        Text(
                                            text = state.errorMessage.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error

                                        )
                                    }

                                    state.searchResults.isEmpty() -> {
                                        Text(
                                            text = stringResource(Res.string.no_search_results),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error

                                        )
                                    }
                                    else -> {
                                        BookList(
                                            books = state.searchResults,
                                            onBookClick = {
                                                onAction(BookListAction.OnBookClick(it))
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                            scrollState = searchResultsListState
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            if(state.favouriteBooks.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.no_favorites),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error

                                )
                            } else {
                                BookList(
                                    books = state.searchResults,
                                    onBookClick = {
                                        onAction(BookListAction.OnBookClick(it))
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                    scrollState = favouriteBookListState
                                )
                            }

                        }

                        2 -> {
                            var selectedVideo by remember { mutableStateOf<String?>(null) } // To track selected video

                            var expandedTest by remember { mutableStateOf(false) }
                            val syllabusTests = listOf(
                                "Test 1" to listOf(
                                    "Self Defence" to listOf(
                                        "Wrist Releases" to listOf("Standard Grip 1 on 1", "Standard Grip 2 on 1", "Inverted Grip 1 on 1", "Inverted Grip 2 on 1", "Inverted Grip - Low"),
                                        "Throws" to listOf("Hip Throw - (O-Goshhi)")),
                                    "Standing Techniques" to listOf("Clinch", "Punches"),
                                    "Technical Techniques" to listOf("Trap and Roll", "Positional Control", "Back Take", "Chokes", "Control", "Submission")
                                ),
                                "Test 2" to listOf(
                                    "Self Defence" to listOf("Grabs", "Escapes"),
                                    "Standing Techniques" to listOf("Knees", "Elbows"),
                                    "Technical Techniques" to listOf("Armbar", "Triangle Choke")
                                ),
                                "Test 3" to listOf(
                                    "Self Defence" to listOf("Grabs", "Escapes"),
                                    "Standing Techniques" to listOf("Knees", "Elbows"),
                                    "Technical Techniques" to listOf("Armbar", "Triangle Choke")
                                ),
                                "Test 4" to listOf(
                                    "Self Defence" to listOf("Grabs", "Escapes"),
                                    "Standing Techniques" to listOf("Knees", "Elbows"),
                                    "Technical Techniques" to listOf("Armbar", "Triangle Choke")
                                )
                            )



                            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                syllabusTests.forEach { (testName, categories) ->
                                    var expanded by remember { mutableStateOf(false) }

                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    ) {
                                        Column {
                                            // Test Name (Clickable)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { expanded = !expanded }
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = testName, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                                                Icon(
                                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                                    contentDescription = "Expand/Collapse"
                                                )
                                            }

                                            // Expandable Categories
                                            AnimatedVisibility(visible = expanded) {
                                                Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                                                    categories.forEach { (category, items) ->
                                                        var categoryExpanded by remember { mutableStateOf(false) }

                                                        Card(
                                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                        ) {
                                                            Column {
                                                                Row(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .clickable { categoryExpanded = !categoryExpanded }
                                                                        .padding(12.dp),
                                                                    verticalAlignment = Alignment.CenterVertically
                                                                ) {
                                                                    Text(text = category, fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.weight(1f))
                                                                    Icon(
                                                                        imageVector = if (categoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                                                        contentDescription = "Expand/Collapse"
                                                                    )
                                                                }

                                                                AnimatedVisibility(visible = categoryExpanded) {
                                                                    Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                                                                        items.forEach { item ->
                                                                            Row(
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth(),
                                                                                verticalAlignment = Alignment.CenterVertically


                                                                            ){
                                                                                Column {
                                                                                    Row(
                                                                                        modifier = Modifier
                                                                                            .fillMaxWidth()
                                                                                            .clickable { categoryExpanded = !categoryExpanded }
                                                                                            .padding(12.dp),
                                                                                        verticalAlignment = Alignment.CenterVertically
                                                                                    ) {
                                                                                        Text(text = "- $item", fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))

                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                        }

                    }
                    }
                }
            }
        }
    }
}


