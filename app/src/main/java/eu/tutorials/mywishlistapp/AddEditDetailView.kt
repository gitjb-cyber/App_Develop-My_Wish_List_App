package eu.tutorials.mywishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eu.tutorials.mywishlistapp.data.Wish
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// 추가 상세 보기 화면
@Composable
fun AddEditDetailView(
    id : Long,
    viewModel: WishViewModel,
    navController: NavController
){
    // 화면 하단에 표시할 메시지(추가된 사항이 있을 때마다 뜸)
    val snackMessage = remember{
        mutableStateOf("")
    }

    // 데이터베이스에 데이터를 저장하는 등의 비동기 메서드를 실행하기 위해 생성
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    // ScaffoldState 를 생성한 후에 위에서 id를 확인해서
    // 0L이 아니면 wish(ViewModel 에서 가져온)를 생성
    if (id != 0L) {
        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    // 뒤로 가기 버튼의 구현
    Scaffold(
        // AppBarView 의 topBar 내부
        topBar = {
            AppBarView(title =
            // res(리소스)file - values - string
            // if (id != 0L) "Update Wish" else "Add Wish"
            if (id != 0L) stringResource(id = R.string.update_wish)
            else stringResource(id = R.string.add_wish)
            )
            // ← 아이콘 버튼을 누르면 뒤로 돌아감
            //  navigateUp : 사용자를 이전에 있던 화면으로 돌아가게 하는 것
            { navController.navigateUp()}
        },
        scaffoldState = scaffoldState
        ) {
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(), // Column 이 content(내용)을 감싸는 것
            // 수평 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp)) // 간격 띄우기 : 10dp

            WishTextField(
                label = "Title(제목)",
                value = viewModel.wishTitleState,
                onValueChanged = {
                    // TextField 의 글을 바꿀 때마다 viewModel 업데이트
                    viewModel.onWishTitleChanged(it) // it 은 바로 밑 WishTextField 에서 가져온 String
                })

            Spacer(modifier = Modifier.height(10.dp)) // 간격 띄우기 : 10dp

            WishTextField(
                label = "Description(설명)",
                value = viewModel.wishDescriptionState,
                onValueChanged = {
                    // TextField 의 글을 바꿀 때마다 viewModel 업데이트
                    viewModel.onWishDescriptionChanged(it) // it 은 바로 밑 WishTextField 에서 가져온 String
                })

            Spacer(modifier = Modifier.height(10.dp)) // 간격 띄우기 : 10dp
            Button(onClick = {
                // 버튼을 클릭했을 때
                // wishTitleState 과 wishDescriptionState 이 비어있지 않다면
                // 그대로 저장하되, Wishlist 항목을 업데이트
                if(viewModel.wishTitleState.isNotEmpty() &&
                    viewModel.wishDescriptionState.isNotEmpty())
                {
                    if (id != 0L) {
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                    }else{
                        // AddWish
                        viewModel.addWish(
                            Wish(
                                // entry 를 수정할 때마다 wishTitleState, wishDescriptionState 가 업데이트
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage.value = "Wish has been created"
                    }
                }else{
                    snackMessage.value = "Enter fields for to create a wish\n(위시를 만들려면 필드를 입력하세요.)"
                }
                scope.launch {
                    // scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                    navController.navigateUp()
                }
            }) {
                Text(
                    text = if (id != 0L) stringResource(id = R.string.update_wish)
                    else stringResource(id = R.string.add_wish),
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )

            }
        }
    }
}
// TextField 의 글을 바꿀 때마다 onValueChange 가 내용을 업데이트
// OutlinedTextField 디자인
@Composable
fun WishTextField(
    label: String,
    value: String,
    // onValueChangeListener 를 받을 때 어떤 작업을 할지 설정
    // 값이 변할 때마다 변한 값의 문자열을 pass(전달) -> 텍스트 안에서 보이는 것을 계속해서 수정
    onValueChanged: (String) -> Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        // OutlinedTextField 어느 부분이든 클릭하면 어떤 키보드가 뜰지 설정 가능
        // 숫자 패드, 이메일 패드(.com), 일반 키보드, 특수 문자 패드 등
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        // 색상 직접 정의 : 텍스트 색상, 텍스트 필드가 강조된 테두리 색상/강조 취소된 테두리 색상
        colors = TextFieldDefaults.outlinedTextFieldColors(
            // using predefined Color(미리 정의된 색상 사용)
            textColor = Color.Black,
            // using our own colors in Res.Values.Color override(리소스에서 직접 정의한 색상 사용)
            focusedBorderColor = colorResource(id = R.color.black), // 강조된 테두리 색상(텍스트 필드 클릭시)
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black)

        )
    )

}

@Preview
@Composable
fun WishTestFieldPrev(){
    WishTextField(label = "text", value = "text", onValueChanged = {})
}