package com.example.a1_akhilboda_34396268_fit2081.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a1_akhilboda_34396268_fit2081.model.entities.FoodIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun upsert(entity: FoodIntakeEntity)

    @Query("SELECT * FROM food_intake WHERE userId = :userId")
    fun observeByUser(userId: Int): Flow<FoodIntakeEntity>

    //Count forms for user
    @Query("SELECT COUNT(*) FROM food_intake WHERE userId = :userId")
    suspend fun countFormsForUser(userId: Int): Int

    @Query("DELETE FROM food_intake WHERE userId = :userId")
    suspend fun deleteForUser(userId: Int)

    @Query("DELETE FROM food_intake")
    suspend fun deleteAll()
}