package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "User_ID")
    val id: Int,

    @ColumnInfo(name = "Password")
    val password: String?,

    @ColumnInfo(name = "PhoneNumber")
    val phoneNumber: String,

    @ColumnInfo(name = "LastName")
    val lastName: String,

    @ColumnInfo(name = "FirstName")
    val firstName: String,

    @ColumnInfo(name = "Sex")
    val sex: String,

    @ColumnInfo(name = "HEIFAtotalscoreMale")
    val heifaTotalScoreMale: String,

    @ColumnInfo(name = "HEIFAtotalscoreFemale")
    val heifaTotalScoreFemale: String,

    @ColumnInfo(name = "DiscretionaryHEIFAscoreMale")
    val discretionaryHeifaScoreMale: String,

    @ColumnInfo(name = "DiscretionaryHEIFAscoreFemale")
    val discretionaryHeifaScoreFemale: String,

    @ColumnInfo(name = "Discretionaryservesize")
    val discretionaryServeSize: String,

    @ColumnInfo(name = "VegetablesHEIFAscoreMale")
    val vegetablesHeifaScoreMale: String,

    @ColumnInfo(name = "VegetablesHEIFAscoreFemale")
    val vegetablesHeifaScoreFemale: String,

    @ColumnInfo(name = "Vegetableswithlegumesallocatedservesize")
    val vegetablesWithLegumesServeSize: String,

    @ColumnInfo(name = "LegumesallocatedVegetables")
    val legumesAllocatedVegetables: String,

    @ColumnInfo(name = "Vegetablesvariationsscore")
    val vegetablesVariationsScore: String,

    @ColumnInfo(name = "VegetablesCruciferous")
    val vegetablesCruciferous: String,

    @ColumnInfo(name = "VegetablesTuberandbulb")
    val vegetablesTuberAndBulb: String,

    @ColumnInfo(name = "VegetablesOther")
    val vegetablesOther: String,

    @ColumnInfo(name = "Legumes")
    val legumes: String,

    @ColumnInfo(name = "VegetablesGreen")
    val vegetablesGreen: String,

    @ColumnInfo(name = "VegetablesRedandorange")
    val vegetablesRedAndOrange: String,

    @ColumnInfo(name = "FruitHEIFAscoreMale")
    val fruitHeifaScoreMale: String,

    @ColumnInfo(name = "FruitHEIFAscoreFemale")
    val fruitHeifaScoreFemale: String,

    @ColumnInfo(name = "Fruitservesize")
    val fruitServeSize: String,

    @ColumnInfo(name = "Fruitvariationsscore")
    val fruitVariationsScore: String,

    @ColumnInfo(name = "FruitPome")
    val fruitPome: String,

    @ColumnInfo(name = "FruitTropicalandsubtropical")
    val fruitTropicalAndSubtropical: String,

    @ColumnInfo(name = "FruitBerry")
    val fruitBerry: String,

    @ColumnInfo(name = "FruitStone")
    val fruitStone: String,

    @ColumnInfo(name = "FruitCitrus")
    val fruitCitrus: String,

    @ColumnInfo(name = "FruitOther")
    val fruitOther: String,

    @ColumnInfo(name = "GrainsandcerealsHEIFAscoreMale")
    val grainsAndCerealsHeifaScoreMale: String,

    @ColumnInfo(name = "GrainsandcerealsHEIFAscoreFemale")
    val grainsAndCerealsHeifaScoreFemale: String,

    @ColumnInfo(name = "Grainsandcerealsservesize")
    val grainsAndCerealsServeSize: String,

    @ColumnInfo(name = "GrainsandcerealsNonwholegrains")
    val grainsAndCerealsNonWholegrains: String,

    @ColumnInfo(name = "WholegrainsHEIFAscoreMale")
    val wholeGrainsHeifaScoreMale: String,

    @ColumnInfo(name = "WholegrainsHEIFAscoreFemale")
    val wholeGrainsHeifaScoreFemale: String,

    @ColumnInfo(name = "Wholegrainsservesize")
    val wholeGrainsServeSize: String,

    @ColumnInfo(name = "MeatandalternativesHEIFAscoreMale")
    val meatAndAlternativesHeifaScoreMale: String,

    @ColumnInfo(name = "MeatandalternativesHEIFAscoreFemale")
    val meatAndAlternativesHeifaScoreFemale: String,

    @ColumnInfo(name = "Meatandalternativeswithlegumesallocatedservesize")
    val meatAndAlternativesWithLegumesServeSize: String,

    @ColumnInfo(name = "LegumesallocatedMeatandalternatives")
    val legumesAllocatedMeatAndAlternatives: String,

    @ColumnInfo(name = "DairyandalternativesHEIFAscoreMale")
    val dairyAndAlternativesHeifaScoreMale: String,

    @ColumnInfo(name = "DairyandalternativesHEIFAscoreFemale")
    val dairyAndAlternativesHeifaScoreFemale: String,

    @ColumnInfo(name = "Dairyandalternativesservesize")
    val dairyAndAlternativesServeSize: String,

    @ColumnInfo(name = "SodiumHEIFAscoreMale")
    val sodiumHeifaScoreMale: String,

    @ColumnInfo(name = "SodiumHEIFAscoreFemale")
    val sodiumHeifaScoreFemale: String,

    @ColumnInfo(name = "Sodiummgmilligrams")
    val sodiumMgMilligrams: String,

    @ColumnInfo(name = "AlcoholHEIFAscoreMale")
    val alcoholHeifaScoreMale: String,

    @ColumnInfo(name = "AlcoholHEIFAscoreFemale")
    val alcoholHeifaScoreFemale: String,

    @ColumnInfo(name = "Alcoholstandarddrinks")
    val alcoholStandardDrinks: String,

    @ColumnInfo(name = "WaterHEIFAscoreMale")
    val waterHeifaScoreMale: String,

    @ColumnInfo(name = "WaterHEIFAscoreFemale")
    val waterHeifaScoreFemale: String,

    @ColumnInfo(name = "Water")
    val water: String,

    @ColumnInfo(name = "WaterTotalmL")
    val waterTotalMl: String,

    @ColumnInfo(name = "BeverageTotalmL")
    val beverageTotalMl: String,

    @ColumnInfo(name = "SugarHEIFAscoreMale")
    val sugarHeifaScoreMale: String,

    @ColumnInfo(name = "SugarHEIFAscoreFemale")
    val sugarHeifaScoreFemale: String,

    @ColumnInfo(name = "Sugar")
    val sugar: String,

    @ColumnInfo(name = "SaturatedFatHEIFAscoreMale")
    val saturatedFatHeifaScoreMale: String,

    @ColumnInfo(name = "SaturatedFatHEIFAscoreFemale")
    val saturatedFatHeifaScoreFemale: String,

    @ColumnInfo(name = "SaturatedFat")
    val saturatedFat: String,

    @ColumnInfo(name = "UnsaturatedFatHEIFAscoreMale")
    val unsaturatedFatHeifaScoreMale: String,

    @ColumnInfo(name = "UnsaturatedFatHEIFAscoreFemale")
    val unsaturatedFatHeifaScoreFemale: String,

    @ColumnInfo(name = "UnsaturatedFatservesize")
    val unsaturatedFatServeSize: String
)
