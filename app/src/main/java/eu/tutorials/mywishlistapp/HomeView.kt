package eu.tutorials.mywishlistapp

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eu.tutorials.mywishlistapp.data.DummyWish
import eu.tutorials.mywishlistapp.data.Wish


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel

) {
    val context = LocalContext.current
    Scaffold (
        // 상단 바
        topBar = { AppBarView(title = "WishList", {
            Toast.makeText(context, "Button Clicked(버튼을 클릭함)", Toast.LENGTH_LONG).show()
        }) },
        // 플로팅 작업 버튼 (+)
        // 클릭 -> navController 가 우리에게 pass(전달)
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp), // 모든 방향 패딩 20 픽셀 밀도
                contentColor = Color.White,
                backgroundColor = Color.Black,
                onClick = {
                    Toast.makeText(context, "FAButton Clicked(버튼을 클릭함)", Toast.LENGTH_LONG).show()
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }) {
                // (+)추가 버튼
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ){
        val wishlist = viewModel.getAllWishes.collectAsState(initial = listOf())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            // 위시리스트 불러오기
            items(wishlist.value, key = {wish -> wish.id}){
                wish ->
                val dismissState = rememberDismissState( // 삭제 상태 저장
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                            viewModel.deleteWish(wish)
                        }
                        true
                    }
                )

                // 스와이프하여 닫기
                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        // 스와이프 상태 -> 배경색 빨강, 아니면 투명
                        val color by animateColorAsState(
                            if (dismissState.dismissDirection
                                == DismissDirection.EndToStart) Color.Red else Color.Transparent
                            ,
                            label = ""
                        )
                        val alignment = Alignment.CenterEnd
                        // 스와이프 할 때 보이는 삭제 안내 아이콘 🗑
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ){
                            Icon(Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.White
                                )
                        }
                    },
                    // 삭제를 어떤 행동으로 정할 것인가
                    // StartToEnd : 왼쪽에서 오른쪽으로 쓸어 넘기기 / EndToStart : 오른쪽에서 왼쪽으로 쓸어 넘기기
                    directions = setOf(DismissDirection.EndToStart),
                    // 스와이프 할 때, 어느 시점에서 dismiss 할 지
                    // 1f : 절반 이상
                    dismissThresholds = {FractionalThreshold(1f)},
                    dismissContent = {
                        WishItem(wish = wish) {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit){
    // 디자인 설정
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable { // 클릭시 수행할 작업
                onClick()
            },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = wish.title, fontWeight = FontWeight.ExtraBold)
            Text(text = wish.description)
        }
    }
}