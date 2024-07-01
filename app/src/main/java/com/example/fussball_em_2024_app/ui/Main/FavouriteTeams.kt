package com.example.fussball_em_2024_app.ui.Main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.model.Team

@Composable
fun FavouriteTeams(leagueName: String, teams: List<Team>, onTeamClick: (Team) -> Unit,
                   viewModel: FavouriteTeamsViewModel = viewModel(
                       factory = FavouriteTeamsViewModelFactory(LocalContext.current)
                   )){

    LaunchedEffect(leagueName) {
        viewModel.loadFavouriteTeams(leagueName)
    }

    val favouriteTeams by viewModel.favouriteTeams.collectAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Favourite Teams",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val favouriteTeamChunks = favouriteTeams.chunked(3)

        favouriteTeamChunks.forEach { chunk ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                chunk.forEach { favTeam ->
                    val team: Team? = teams.find { it.teamName == favTeam.teamName }
                    if (team != null) {
                        Column(
                            modifier = Modifier
                                .clickable { onTeamClick(team) }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = team.teamIconUrl),
                                contentDescription = "Logo von ${team.teamName}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = team.teamName,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .clickable { viewModel.removeFavouriteTeam(favTeam) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-", style = TextStyle(color = Color.White, fontSize = 24.sp))
                            }

                        }
                    }
                }

                // Add the Add button in the last row if not full and it's the last chunk
                if (chunk.size < 3 && chunk == favouriteTeamChunks.last()) {
                    Column(
                        modifier = Modifier
                            .clickable { isDropdownExpanded = true }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", style = TextStyle(color = Color.White, fontSize = 24.sp))
                        }
                        Text(
                            text = "Add",
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }
        }

        if (favouriteTeamChunks.isEmpty()) {
            Column(
                modifier = Modifier
                    .clickable { isDropdownExpanded = true }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", style = TextStyle(color = Color.White, fontSize = 24.sp))
                }
                Text(
                    text = "Add",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }

        // DropdownMenu for selecting a team to add
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            teams.forEach { team ->
                DropdownMenuItem(
                    text = { Text(team.teamName) },
                    onClick = {
                        viewModel.addFavouriteTeam(team)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}