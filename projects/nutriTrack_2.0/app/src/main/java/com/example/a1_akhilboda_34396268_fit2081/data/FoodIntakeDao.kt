// FoodIntakeDao.kt
package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FoodIntakeEntity)

    @Query("SELECT * FROM food_intake WHERE userId = :uid LIMIT 1")
    fun observeForUser(uid: Int): Flow<FoodIntakeEntity?>
}
