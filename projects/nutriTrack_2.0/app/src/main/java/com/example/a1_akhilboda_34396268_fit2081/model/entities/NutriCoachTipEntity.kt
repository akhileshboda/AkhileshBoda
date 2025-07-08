package com.example.a1_akhilboda_34396268_fit2081.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "nutricoach_tips")
data class NutriCoachTipEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "tip_text") val tipText: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = Instant.now().toEpochMilli()
)