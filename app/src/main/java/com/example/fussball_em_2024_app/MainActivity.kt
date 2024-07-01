package com.example.fussball_em_2024_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fussball_em_2024_app.ui.MatchDetail.MatchDetailScreen
import com.example.fussball_em_2024_app.ui.TeamDetail.TeamDetailScreen
import com.example.testjetpackcompose.ui.theme.TestJetpackComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FussballEMApp()
        }

    }
}

@Composable
fun FussballEMApp(){
    TestJetpackComposeTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = Overview.route) {
            composable(route = Overview.route){
                MatchScreen(navController)
            }

            composable(
                route = TeamDetail.routeWithArgs,
                arguments = TeamDetail.arguments){ backStackEntry ->
                val teamId = backStackEntry.arguments?.getInt(TeamDetail.teamIdArg) ?: 0
                TeamDetailScreen(teamId = teamId, navController)
            }

            composable(
                route = MatchDetail.routeWithArgs,
                arguments = MatchDetail.arguments){ backStackEntry ->
                val matchId = backStackEntry.arguments?.getInt(MatchDetail.matchIdArg) ?: 0
                MatchDetailScreen(matchId = matchId, navController)
            }
        }
    }
}




