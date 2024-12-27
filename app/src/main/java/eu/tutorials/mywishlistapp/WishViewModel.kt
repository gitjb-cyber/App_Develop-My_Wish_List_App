package eu.tutorials.mywishlistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.mywishlistapp.data.Wish
import eu.tutorials.mywishlistapp.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel 상속
class WishViewModel(
    private val wishRepository: WishRepository = Graph.wishRepository
):ViewModel() {

    var wishTitleState by mutableStateOf("") // 위시 제목의 상태
    var wishDescriptionState by mutableStateOf("") // 위시 설명의 상태

    // ViewModel 을 상속받은 WishViewModel 에게 wishTitleState 를 사용하게 명령
    // 항목의 title(제목)을 수정 할 때마다 mutable State(가변적인 상태)
    fun onWishTitleChanged(newString: String){
        // wishTitleState 는 다른 새 문자열로 overwrite(덮어쓰기)
        wishTitleState = newString
    }

    fun onWishDescriptionChanged(newString: String){
        wishDescriptionState = newString
    }

    // lateinit : 늦은 초기화. 결과값을 기반으로 getAllWish 초기화(값이 계속해서 바뀔 수 있다)(val 사용 X)
    // 초기화가 확실하지 않거나 해당 속성에 접근할 때마다 null 체크를 하고 싶지 않을 때 사용
    // Flow 사용은 비동기 상태
    lateinit var getAllWishes: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWishes = wishRepository.getWishes()
        }
    }

    fun addWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAWish(wish = wish)
        }
    }

    fun getAWishById(id:Long):Flow<Wish> {
        return wishRepository.getAWishById(id)
    }

    fun updateWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wish = wish)
        }
    }

    fun deleteWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish = wish)
        }
    }
}