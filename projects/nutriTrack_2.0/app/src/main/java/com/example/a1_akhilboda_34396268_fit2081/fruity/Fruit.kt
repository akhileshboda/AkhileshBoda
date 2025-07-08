package com.example.a1_akhilboda_34396268_fit2081.fruity

data class FruitNutrition(
    val carbohydrates: Double,
    val protein:       Double,
    val fat:           Double,
    val calories:      Int,
    val sugar:         Double
)

data class Fruit(
    val genus:      String,
    val name:       String,
    val family:     String,
    val order:      String,
    val nutritions: FruitNutrition
)