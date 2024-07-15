package com.example.fussball_em_2024_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.ui.Main.FavouriteTeams
import com.example.fussball_em_2024_app.ui.Main.LastMatchScreen
import com.example.fussball_em_2024_app.ui.Main.NextMatchScreen
import com.example.fussball_em_2024_app.utils.DateFormater.formatDate
import com.example.fussball_em_2024_app.viewModels.MatchViewModel
import com.example.fussball_em_2024_app.viewModels.MatchViewModelFactory
import com.example.fussball_em_2024_app.viewModels.TeamViewModel

@Composable
fun MatchScreen(navController: NavController, modifier: Modifier = Modifier) {
    val teamViewModel: TeamViewModel = viewModel()
    val viewState by teamViewModel.teamState
    val matchViewModel: MatchViewModel = viewModel(factory = MatchViewModelFactory(LocalContext.current))
    val nextViewState by matchViewModel.nextMatchState
    val lastViewState by matchViewModel.lastMatchState

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                // Zeigt den Ladekreis in der Mitte des Bildschirms an
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                Text("ERROR OCCURRED", color = LocalTextColor.current)
            }
            else -> {
                Column (
                    modifier = modifier
                        .fillMaxSize()
                )
                {
                    // Zeige zuerst den nächsten Match an
                    nextViewState.match?.let { match ->
                        NextMatchScreen(match = match, navController = navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    lastViewState.match?.let { match ->
                        LastMatchScreen(match = match, navController = navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Favourite Teams Section
                    FavouriteTeams("em24", viewState.list, { selectedTeam ->
                        navController.navigate("${TeamDetail.route}/${selectedTeam.teamId}")
                    })

                    // Zeige dann die Liste der CategoryScreen Matches
                    if (viewState.list.isNotEmpty()) {
                        TeamScreen(teams = viewState.list, navController = navController)
                    } else {
                        Text("No Such items Found.")
                    }
                }

                Button(
                    onClick = { navController.navigate("overview") },
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text("Go to leagues", color = LocalTextColor.current)
                }
            }
        }
    }
}

@Composable
fun TeamScreen(teams: List<Team>, navController: NavController) {

    Column {
        Text(
            text="All Teams", color = LocalTextColor.current
        )
    }

    // Eine LazyColumn ist bereits scrollbar
    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp), // Füge Abstand der ganzen Liste hinzu
        modifier = Modifier.fillMaxSize()
    ) {
        items(teams) { team ->
            TeamItem(team = team, onTeamClick = { selectedTeam ->
                navController.navigate("${TeamDetail.route}/${selectedTeam.teamId}")
            })
        }
    }
}

@Composable
fun MatchItems(match: Match) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Oberer Teil mit den Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle image with team name for team1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(model = match.team1.teamIconUrl),
                    contentDescription = "Logo von ${match.team1.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = match.team1.teamName,
                    textAlign = TextAlign.Center,
                    color = LocalTextColor.current
                )
            }

            // Datum des Spiels in der Mitte
            Text(
                text = formatDate(match.matchDateTime),  // Datum formatieren nach Bedarf
                color = LocalTextColor.current,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Circle image with team name for team2
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(model = match.team2.teamIconUrl),
                    contentDescription = "Logo von ${match.team2.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = match.team2.teamName,
                    textAlign = TextAlign.Center,
                    color = LocalTextColor.current
                )
            }
        }
    }
}


@Composable
fun TeamItem(team:Team, onTeamClick: (Team) -> Unit){
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { onTeamClick(team) }) {
        // Oberer Teil mit den Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle image with team name for team1
            Row {
                Image(
                    painter = rememberAsyncImagePainter(model = team.teamIconUrl),
                    contentDescription = "Logo von ${team.teamName}",
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),  // Macht das Bild kreisförmig
                    contentScale = ContentScale.Crop
                )
                Column {
                    team.teamGroupName?.let {
                        Text(
                            text= it,
                            textAlign = TextAlign.Center,
                            color = LocalTextColor.current
                            )
                    }
                    Text(
                        text = team.teamName,
                        textAlign = TextAlign.Center,
                        color = LocalTextColor.current
                    )
                }

            }

            // Datum des Spiels in der Mitte

        }
    }

}




