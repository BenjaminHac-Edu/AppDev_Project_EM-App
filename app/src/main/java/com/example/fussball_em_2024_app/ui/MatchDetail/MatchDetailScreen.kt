package com.example.fussball_em_2024_app.ui.MatchDetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fussball_em_2024_app.LocalColors
import com.example.fussball_em_2024_app.R
import com.example.fussball_em_2024_app.getMatchData
import com.example.fussball_em_2024_app.model.Goal
import com.example.fussball_em_2024_app.model.OpenAIResponse
import com.example.fussball_em_2024_app.utils.DateFormater
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModel
import com.example.fussball_em_2024_app.viewModels.MatchDetailViewModelFactory
import com.example.testjetpackcompose.ui.theme.buttonsColor
import com.example.testjetpackcompose.ui.theme.darkGreen
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun MatchDetailScreen(
    matchId: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val matchDetailViewModel: MatchDetailViewModel = viewModel(
        factory = MatchDetailViewModelFactory(matchId)
    )
    val matchInfo by matchDetailViewModel.matchState

    var prediction by remember { mutableStateOf("Loading...") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
    // Fetch prediction
    LaunchedEffect(matchInfo.match) {
        matchInfo.match?.let { match ->
            val team1Name = match.team1?.teamName
            val team2Name = match.team2?.teamName
            if (team1Name != null && team2Name != null) {
                coroutineScope.launch {
                    try {
                        val prompt = """
                        Generate a JSON object for the expected outcome of the match between $team1Name and $team2Name. The Prediction should be based on the last 4 matches between the  teams. The JSON should have the following structure:
                        {
                            "team1": "$team1Name",
                            "team2": "$team2Name",
                            "expectedOutcome": {
                                "team1Score": <integer>,
                                "team2Score": <integer>
                                
                            }
                        }
                        """.trimIndent()
                        val data: OpenAIResponse? = getMatchData(prompt)
                        val jsonString = data?.choices?.firstOrNull()?.message?.content ?: "{}"
                        Log.d("OpenAIResponse", jsonString)  // Log the response
                        val jsonObject = JSONObject(jsonString)
                        val team1 = jsonObject.optString("team1", "Unknown")
                        val team2 = jsonObject.optString("team2", "Unknown")
                        val expectedOutcome = jsonObject.optJSONObject("expectedOutcome")
                        val team1Score = expectedOutcome?.optInt("team1Score", 0) ?: 0
                        val team2Score = expectedOutcome?.optInt("team2Score", 0) ?: 0


                        prediction = "$team1 $team1Score - $team2Score $team2"
                    } catch (e: Exception) {
                        e.printStackTrace()
                        prediction = "Error fetching prediction: ${e.message}"
                    }
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            matchInfo.error != null || matchInfo.match == null -> {
                Text("ERROR OCCURRED", color = LocalColors.current.textColor)
            }

            else -> {
                val match = matchInfo.match!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Game: ${match.getTeamVsNames}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        textAlign = TextAlign.Center,
                        color = LocalColors.current.textColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(horizontalAlignment = Alignment.Start) {
                            Image(
                                painter = rememberAsyncImagePainter(model = match.team1?.teamIconUrl),
                                contentDescription = "Logo von ${match.team1?.teamName}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),  // Macht das Bild kreisförmig
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column {
                            Text(
                                text = (if(match.group?.groupName?.length!! > 1) match.group.groupName else "Group ${match.group.groupName}"),
                                style = TextStyle(fontSize = 18.sp),
                                textAlign = TextAlign.Center,
                                color = LocalColors.current.textColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Image(
                                painter = rememberAsyncImagePainter(model = match.team2?.teamIconUrl),
                                contentDescription = "Logo von ${match.team2?.teamName}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),  // Macht das Bild kreisförmig
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (match.matchIsFinished == true) {
                        TextDetailMatchInformation(text = "Finished\n" + "Started: ${DateFormater.formatDate(match.matchDateTime)}")
                    }
                    else if(DateFormater.isDateAfterNow(match.matchDateTimeUTC)){
                        TextDetailMatchInformation(text = "Ongoing")
                    }
                    else{
                        TextDetailMatchInformation(text = "Not Started\n" + "Starting at: ${DateFormater.formatDate(match.matchDateTime)}")
                    }

                    if(match.location != null){
                        if(match.location!!.locationStadium == null){
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationCity}")
                        }else if(match.location!!.locationCity == null){
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationStadium}")
                        }else{
                            TextDetailMatchInformation(text = "Stadion: ${match.location?.locationStadium} (${match.location?.locationCity})")
                        }
                    }

                    if (match.numberOfViewers != null){
                        TextDetailMatchInformation(text = "Number of Viewers: ${match.numberOfViewers}")
                    }


                    if (match.matchIsFinished) {
                        val team1Score = match.goals?.lastOrNull()?.scoreTeam1 ?: 0
                        val team2Score = match.goals?.lastOrNull()?.scoreTeam2 ?: 0
                        Text(
                            text = "Result: ${match.team1.teamName} $team1Score - $team2Score ${match.team2.teamName}",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            textAlign = TextAlign.Center,
                            color = LocalColors.current.textColor,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }


                    // Display prediction
                    Text(
                        text = "Prediction: $prediction",
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
                        textAlign = TextAlign.Center,
                        color = LocalColors.current.textColor,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // goals
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(match.team1.teamName, color = LocalColors.current.textColor)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(match.team2.teamName, color = LocalColors.current.textColor)
                        }
                    }

                    HorizontalDivider(color = LocalColors.current.textColor, thickness = 3.dp)

                    Spacer(modifier = Modifier.height(20.dp))

                    var team1GoalNumber = 0
                    var team2GoalNumber = 0

                    if(match.matchIsFinished && match.goals == null || match.goals!!.isEmpty())
                        Text(text = "No goal data available", color = LocalColors.current.textColor)

                    else{
                        match.goals!!.forEach{ goal ->
                            if (goal.scoreTeam1!! > team1GoalNumber){
                                team1GoalNumber++
                                GoalItem(goal, true)
                            }
                            else if (goal.scoreTeam2!! > team2GoalNumber){
                                team2GoalNumber++
                                GoalItem(goal, false)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Back button
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonsColor, // Farbe des Buttons
                            contentColor = Color.White,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)

                            .padding(top = 16.dp)
                    ) {
                        Text("Go back", color = Color.White)
                    }

                }

            }
        }

    }
}}


@Composable
fun GoalItem(goal: Goal, isFirstTeam: Boolean) {
    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = if (isFirstTeam) Alignment.Start else Alignment.End){
        Column(modifier = Modifier.fillMaxWidth(0.5f), horizontalAlignment = if (isFirstTeam) Alignment.End else Alignment.Start) {
           val imageColor = if (LocalColors.current.textColor == Color.White) Color.White else Color.Black
            Row {
                if (isFirstTeam){
                    Text(
                        text = if(goal.matchMinute != null) (goal.matchMinute.toString() + "' ") else "no data ",
                        color = LocalColors.current.textColor
                    )


                    Image(
                        painter = painterResource(id = R.drawable.football),
                        contentDescription = "football",
                        colorFilter = ColorFilter.tint(imageColor),

                    )
                }else {
                    Image(
                        painter = painterResource(id = R.drawable.football),
                        contentDescription = "football",
                        colorFilter = ColorFilter.tint(imageColor)
                    )
                    Text(
                        text = if(goal.matchMinute != null) (" " + goal.matchMinute.toString() + "'") else " no data",
                        color = LocalColors.current.textColor
                    )
                }
            }
        }

        if(goal.comment != null){
            Text(
                text = "${goal.getGoalGetterName}\n" +
                        "${goal.comment}",
                textAlign = TextAlign.Center,
                color = LocalColors.current.textColor
            )
        }
        else if(goal.isOwnGoal == true){
            Text(
                text = "${goal.getGoalGetterName}\n" +
                        "(OG)",
                textAlign = TextAlign.Center,
                color = LocalColors.current.textColor
            )
        }
        else{
            Text(
                text = goal.getGoalGetterName,
                textAlign = TextAlign.Center,
                color = LocalColors.current.textColor
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun TextDetailMatchInformation(text: String){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = LocalColors.current.textColor,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
