package eu.tutorials.mywishlistapp

import android.content.Context
import androidx.room.Room
import eu.tutorials.mywishlistapp.data.WishDatabase
import eu.tutorials.mywishlistapp.data.WishRepository

// object 로 singleton(싱글톤) 선언
// singleton 은 애플리케이션에 단 하나의 instance 만 존재하는 클래스(= 다른 이름을 부여하는 다른 Graph 를 만들 수 없음)
object Graph {
    lateinit var database: WishDatabase

    val wishRepository by lazy{
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
    }
    
}