package com.example.a1_akhilboda_34396268_fit2081.model.repositories

import com.example.a1_akhilboda_34396268_fit2081.model.daos.FoodIntakeDao
import com.example.a1_akhilboda_34396268_fit2081.model.entities.FoodIntakeEntity

class FoodIntakeRepository(private val dao: FoodIntakeDao) {
    fun getUserForm(userId: Int) = dao.observeByUser(userId)
    suspend fun countFormsForUser(userId: Int) = dao.countFormsForUser(userId)
    suspend fun save(entity: FoodIntakeEntity) = dao.upsert(entity)
}