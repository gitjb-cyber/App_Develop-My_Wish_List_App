package eu.tutorials.mywishlistapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

// navigation(탐색)에 대한 composable 설정
@Composable
fun Navigation(viewModel: WishViewModel = viewModel(),
               navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ){
        // 처음 화면
        composable(Screen.HomeScreen.route){
            HomeView(navController, viewModel)
        }
        // 추가된 화면(다음 화면)
        // HomeView 에서 버튼 클릭할 때 실행
        composable(
            Screen.AddScreen.route + "/{id}",
            // AddEditDetailView(id = id, ... 로 쓰기 위한 작업
            // 기존 : AddEditDetailView(id = 0L, ...
            arguments = listOf(navArgument("id"){
                type = NavType.LongType
                defaultValue = 0L
                nullable = false
            }
            )
        ){entry ->
            val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditDetailView(id = id, viewModel = viewModel, navController = navController)
        }
    }
}