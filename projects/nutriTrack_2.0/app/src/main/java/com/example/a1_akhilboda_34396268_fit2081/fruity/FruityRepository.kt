package com.example.a1_akhilboda_34396268_fit2081.fruity

class FruityRepository(private val api: FruityService) {
    suspend fun fetchFruit(name: String): Fruit = api.getFruit(name.trim().lowercase())
}
