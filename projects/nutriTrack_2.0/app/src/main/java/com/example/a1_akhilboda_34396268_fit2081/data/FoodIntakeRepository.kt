// FoodIntakeRepository.kt
package com.example.a1_akhilboda_34396268_fit2081.data

class FoodIntakeRepository(private val dao: FoodIntakeDao) {
    fun observeForUser(userId: Int) = dao.observeForUser(userId)
    suspend fun save(entity: FoodIntakeEntity) = dao.upsert(entity)
}
