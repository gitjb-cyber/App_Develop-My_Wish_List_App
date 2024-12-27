package eu.tutorials.mywishlistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 영구 저장 목록 : | id | title | description |
@Entity(tableName = "wish-table")
data class Wish( // Wish 데이터 클래스는 DAO를 통해 영구 저장
    // wish-table 안에 새로운 항목을 생성할 때마다 autoGenerate = true 로 인해 id : 1 -> 2 -> 3 -> ... 순으로 증가
    // id 의 1, 2, 3 항목을 지워도 다음으로 생성되는건 4
    @PrimaryKey(autoGenerate = true) // 자동으로 증가
    val id: Long = 0L,

    // ColumnInfo 는 열의 이름을 지정
    @ColumnInfo(name = "wish-title")
    val title: String = "",
    @ColumnInfo(name = "wish-desc")
    val description: String = ""
)

// 시험용 더미 데이터
object DummyWish {
    val wishList = listOf(
        Wish(title = "Google Watch 2", description = "An android Watch"),
        Wish(title = "Galaxy Watch 8", description = "An android Watch"),
        Wish(title = "Galaxy S25", description = "An android Phone"),
        Wish(title = "Galaxy Tab S10", description = "An android Tablet"),
        Wish(title = "Iphone 13", description = "An iOS Phone"),
    )
}