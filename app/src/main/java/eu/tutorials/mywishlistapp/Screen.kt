package eu.tutorials.mywishlistapp

// sealed class : inherit(상속) 불가능 클래스
// navigation(탐색)을 위해 사용할 두 개의 화면
sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_Screen")
    object AddScreen : Screen("add_screen")

}