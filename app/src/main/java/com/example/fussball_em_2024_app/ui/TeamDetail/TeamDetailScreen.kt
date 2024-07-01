package com.example.fussball_em_2024_app.ui.TeamDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LastMatchScreen
import com.example.fussball_em_2024_app.NextMatchScreen
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModel
import com.example.fussball_em_2024_app.viewModels.TeamDetailViewModelFactory

@Composable
fun TeamDetailScreen(teamId: Int, navController: NavController, modifier: Modifier = Modifier) {
    val teamDetailViewModel: TeamDetailViewModel = viewModel(
        factory = TeamDetailViewModelFactory(teamId)
    )
    val teamInfo by teamDetailViewModel.teamInfoState
    val nextMatch by teamDetailViewModel.nextMatchState
    val lastMatch by teamDetailViewModel.lastMatchState

    Box(modifier = Modifier.fillMaxSize()) {
        when{
            teamInfo.error != null -> {
                Text("ERROR OCCURRED")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    // Team name as a headline
                    teamInfo.teamInfo?.teamName?.let {
                        Text(
                            text = it,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    Image(
                        painter = rememberAsyncImagePainter(model = teamInfo.teamInfo?.teamIconUrl),
                        contentDescription = "Logo von ${teamInfo.teamInfo?.teamName}",
                        modifier = Modifier
                            .size(60.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape),  // Macht das Bild kreisförmig
                        contentScale = ContentScale.Crop
                    )

                    // Team details
                    Text(
                        text = "Points: ${teamInfo.teamInfo?.points}",
                        style = TextStyle(fontSize = 30.sp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Column {
                            Text(
                                text = "Wins: ${teamInfo.teamInfo?.won}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Losses: ${teamInfo.teamInfo?.lost}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Draws: ${teamInfo.teamInfo?.draw}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Goals: ${teamInfo.teamInfo?.goals}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Opponent Goals: ${teamInfo.teamInfo?.opponentGoals}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Goal Diff: ${teamInfo.teamInfo?.goalDiff}",
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    nextMatch.match?.let { match ->
                        NextMatchScreen(match = match, navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    lastMatch.match?.let { match ->
                        LastMatchScreen(match = match, navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Fills remaining space

                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text("← Back")
                    }
                }
            }
        }
    }
}