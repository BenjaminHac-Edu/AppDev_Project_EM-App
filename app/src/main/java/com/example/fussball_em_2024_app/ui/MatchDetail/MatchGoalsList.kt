package com.example.fussball_em_2024_app.ui.MatchDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fussball_em_2024_app.model.Goal

@Composable
fun MatchGoalsList(
    teamName1: String,
    teamName2: String,
    goals: List<Goal>,
    modifier: Modifier = Modifier
){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(teamName1)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(teamName2)
        }
    }

    HorizontalDivider(color = Color.Black, thickness = 3.dp)

    Spacer(modifier = Modifier.height(15.dp))

    Box(modifier = modifier){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var team1GoalNumber = 0
            var team2GoalNumber = 0

            Column(horizontalAlignment = Alignment.Start) {
                goals.forEach { goal ->
                    if (goal.scoreTeam1!! > team1GoalNumber){
                        team1GoalNumber++
                        Text(
                            text = "${goal.matchMinute}'\n${goal.goalGetterName}",
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
            VerticalDivider(color = Color.Black, thickness = 3.dp)
            Column(horizontalAlignment = Alignment.End) {
                goals.forEach { goal ->
                    if (goal.scoreTeam2!! > team1GoalNumber){
                        team2GoalNumber++
                        Text(
                            text = "${goal.matchMinute}'\n${goal.goalGetterName}",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


}