package com.example.a1_akhilboda_34396268_fit2081.model.repositories

import com.example.a1_akhilboda_34396268_fit2081.model.daos.NutriCoachDao
import com.example.a1_akhilboda_34396268_fit2081.model.entities.NutriCoachTipEntity
import kotlinx.coroutines.flow.Flow

class NutriCoachRepository(
    private val tipDao: NutriCoachDao
) {
    /**
     * Store a new tip for the given user.
     */
    suspend fun insertTip(userId: Int, text: String) {
        val tip = NutriCoachTipEntity(userId = userId, tipText = text)
        tipDao.insertTip(tip)
    }

    /**
     * Get a flow of all tips for this user, newest first.
     */
    fun getTips(userId: Int): Flow<List<NutriCoachTipEntity>> = tipDao.getTips(userId)

    /**
     * Clear all stored tips for a user (optional).
     */
    suspend fun clearAllTips(userId: Int) {
        tipDao.clearUserTips(userId)
    }
}