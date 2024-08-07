package com.example.fussball_em_2024_app.ui.main

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.LocalLeague
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.ui.SimpleText
import com.example.fussball_em_2024_app.ui.TeamFlagImage
import com.example.fussball_em_2024_app.ui.TextAlignCenter
import com.example.fussball_em_2024_app.viewModels.FavouriteTeamsViewModel
import com.example.fussball_em_2024_app.viewModels.FavouriteTeamsViewModelFactory

@Composable
fun FavouriteTeams(teams: List<Team>, onTeamClick: (Team) -> Unit){
    val league = LocalLeague.current
    val leagueName = league.leagueShortcut + league.leagueSeason

    val viewModel: FavouriteTeamsViewModel = viewModel(
        factory = FavouriteTeamsViewModelFactory(LocalContext.current)
    )

    SideEffect {
        viewModel.loadFavouriteTeams(leagueName)
    }

    val favouriteTeams by viewModel.favouriteTeams.collectAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SimpleText(
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
                        FavouriteTeamItem(team = team, onTeamClick = onTeamClick)  {
                            viewModel.removeFavouriteTeam(favTeam, leagueName)
                        }
                    }
                }

                // Add the Add button in the last row if not full and it's the last chunk
                if (chunk.size < 3 && chunk == favouriteTeamChunks.last()) {
                    FavouriteTeamAddButton { isDropdownExpanded = true }
                }
            }
        }

        if (favouriteTeamChunks.isEmpty()) {
            FavouriteTeamAddButton { isDropdownExpanded = true }
        }

        // DropdownMenu for selecting a team to add
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.background(LocalColors.current.secondaryBackgroundColor),
        ) {
            teams.forEach { team ->
                DropdownMenuItem(
                    text = { SimpleText(team.teamName) },
                    onClick = {
                        viewModel.addFavouriteTeam(team, leagueName)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FavouriteTeamItem(team: Team, onTeamClick: (Team) -> Unit, onMinusClick: () -> Unit){
    Column(
        modifier = Modifier
            .clickable { onTeamClick(team) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TeamFlagImage(team = team, size = 50.dp)
        Spacer(modifier = Modifier.width(8.dp))
        TextAlignCenter(
            text = team.teamName.replace(" ", "\n"),
            style = TextStyle(fontSize = 14.sp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Red)
                .clickable { onMinusClick() },
            contentAlignment = Alignment.Center
        ) {
            SimpleText("-", style = TextStyle(color = Color.White, fontSize = 24.sp))
        }
    }
}

@Composable
fun FavouriteTeamAddButton(onClick: () -> Unit){
    Column(
        modifier = Modifier
            .clickable { onClick() }
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
            SimpleText("+", style = TextStyle(fontSize = 24.sp))
        }
        TextAlignCenter(
            text = "Add",
            style = TextStyle(fontSize = 14.sp)
        )
    }
}