package uk.ac.tees.mad.d3901263.screens.liked

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.homescreen.SalonItemCard
import uk.ac.tees.mad.d3901263.screens.liked.viewmodel.LikedItemViewModel
import uk.ac.tees.mad.d3901263.screens.profile.ProfileHeader

@Composable
fun LikedItemScreen(
    onBack: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val viewModel: LikedItemViewModel = hiltViewModel()
    val context = LocalContext.current
    val likedItemsListState by viewModel.likedItemsListState.collectAsState(initial = null)
    val likedItemList = likedItemsListState?.isSuccess
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ProfileHeader(
            onBack = onBack,
            title = "Liked Salons"
        )
        if (likedItemsListState?.isLoading == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (likedItemList.isNullOrEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "No liked items")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(likedItemList) { salon ->
                        SalonItemCard(
                            salon = salon,
                            onClick = {
                                onItemClick(salon.id)
                            },
                            onLike = {
                                viewModel.deleteFromFavorite(salon, context)
                            },
                            unLike = true
                        )
                    }
                }
            }
        }
    }
}

object LikedItem : Navigation {
    override val route = "liked"
    override val titleRes: Int = R.string.app_name
}