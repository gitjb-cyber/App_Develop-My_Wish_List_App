package eu.tutorials.mywishlistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO 추상 클래스 +CRUD
@Dao
abstract class WishDao {

    // Insert : 함수가 데이터베이스에 뭔가 삽입하는 과정을 담당
    // onConflict : 충돌 시(같은 id를 가진 wish 를 추가하는 등 일어나면 안되는 일)
    // OnConflictStrategy.IGNORE : 충돌 전략.상관하지 않고 계속 진행(ABORT : 그 항목을 중단, REPLACE : 대체)
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Create
    abstract fun addAWish (wishEntity: Wish) // 추상 메서드를 Entity 에서 구현

    // Query : 데이터베이스에서 load 로 가져오고 싶은 것이 뭔지 정의
    // wish-table 에 있는 모든 wish 항목을 load 로 가져옴
    @Query("Select * from `wish-table`") // Read
    abstract fun getAllWishes(): Flow<List<Wish>>

    // Update : 표 안에 있는 entry 를 수정할 수 있음
    @Update // Update
    abstract fun updateAWish(wishEntity: Wish)

    @Delete // Delete
    abstract fun deleteAWish(wishEntity: Wish)

    // load 로 가져오려는 id 를 전달(:id는 필터)
    // wish-table 에 있는 모든 wish 항목을 가져오지만 필터에 의해
    // Id = id 인 경우, 즉 id로 wish 를 가져오기 위해 전달하는 것만 가져올 것
    @Query("Select * from `wish-table` where id = :id")
    abstract fun getAWishById(id: Long): Flow<Wish>
}