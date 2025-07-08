package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intake",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],    // whatever your UserEntity primary‐key field is actually called
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId")            // helps Room join more efficiently
    ]
)
data class FoodIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val selectedFoods: String,    // comma-separated
    val persona: String,
    val largestMealTime: String,
    val wakeUpTime: String,
    val bedTime: String
)
