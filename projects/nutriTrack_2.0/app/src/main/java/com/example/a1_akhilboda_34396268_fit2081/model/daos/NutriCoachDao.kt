package com.example.a1_akhilboda_34396268_fit2081.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a1_akhilboda_34396268_fit2081.model.entities.NutriCoachTipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTip(tip: NutriCoachTipEntity)

    @Query("SELECT * FROM nutricoach_tips WHERE user_id = :userId ORDER BY created_at DESC")
    fun getTips(userId: Int): Flow<List<NutriCoachTipEntity>>

    @Query("DELETE FROM nutricoach_tips WHERE user_id = :userId")
    suspend fun clearUserTips(userId: Int)
}
