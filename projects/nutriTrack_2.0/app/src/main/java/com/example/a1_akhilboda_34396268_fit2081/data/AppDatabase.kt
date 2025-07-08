package com.example.a1_akhilboda_34396268_fit2081.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Database(entities = [UserEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // destroy table if there are no migration rules defined, avoids having to increment version
                    .build()
                INSTANCE = instance

                // Launch a coroutine scope which check if database is already populated
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val userDao = instance.userDao()
                        if (userDao.countUsers() == 0) {
                            // call population function
                            populateDatabase(context, userDao)
                        }
                        else {
                            // Log message for maintenance
                            Log.d("AppDatabase", "Database already populated")
                        }
                    } catch (e: Exception) {
                        Log.e("AppDatabase", "Error prepopulating database", e)
                    }
                }
                instance
            }
        }
    }
}

private suspend fun populateDatabase(context: Context, userDao: UserDao) {
    val inputStream = context.assets.open("users.csv")
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.useLines { lines ->
        lines.drop(1).forEach { line ->
            val fields = line.split(",").map { it.trim() }

            try {
                val user = UserEntity(
                    id = fields[1].toIntOrNull() ?: return@forEach,
                    password = null,
                    phoneNumber = fields[0].toString(),
                    lastName = fields[2].toString(),
                    firstName = fields[3].toString(),
                    sex = fields[4].toString(),
                    heifaTotalScoreMale = fields[5].toString(),
                    heifaTotalScoreFemale = fields[6].toString(),
                    discretionaryHeifaScoreMale = fields[7].toString(),
                    discretionaryHeifaScoreFemale = fields[8].toString(),
                    discretionaryServeSize = fields[9].toString(),
                    vegetablesHeifaScoreMale = fields[10].toString(),
                    vegetablesHeifaScoreFemale = fields[11].toString(),
                    vegetablesWithLegumesServeSize = fields[12].toString(),
                    legumesAllocatedVegetables = fields[13].toString(),
                    vegetablesVariationsScore = fields[14].toString(),
                    vegetablesCruciferous = fields[15].toString(),
                    vegetablesTuberAndBulb = fields[16].toString(),
                    vegetablesOther = fields[17].toString(),
                    legumes = fields[18].toString(),
                    vegetablesGreen = fields[19].toString(),
                    vegetablesRedAndOrange = fields[20].toString(),
                    fruitHeifaScoreMale = fields[21].toString(),
                    fruitHeifaScoreFemale = fields[22].toString(),
                    fruitServeSize = fields[23].toString(),
                    fruitVariationsScore = fields[24].toString(),
                    fruitPome = fields[25].toString(),
                    fruitTropicalAndSubtropical = fields[26].toString(),
                    fruitBerry = fields[27].toString(),
                    fruitStone = fields[28].toString(),
                    fruitCitrus = fields[29].toString(),
                    fruitOther = fields[30].toString(),
                    grainsAndCerealsHeifaScoreMale = fields[31].toString(),
                    grainsAndCerealsHeifaScoreFemale = fields[32].toString(),
                    grainsAndCerealsServeSize = fields[33].toString(),
                    grainsAndCerealsNonWholegrains = fields[34].toString(),
                    wholeGrainsHeifaScoreMale = fields[35].toString(),
                    wholeGrainsHeifaScoreFemale = fields[36].toString(),
                    wholeGrainsServeSize = fields[37].toString(),
                    meatAndAlternativesHeifaScoreMale = fields[38].toString(),
                    meatAndAlternativesHeifaScoreFemale = fields[39].toString(),
                    meatAndAlternativesWithLegumesServeSize = fields[40].toString(),
                    legumesAllocatedMeatAndAlternatives = fields[41].toString(),
                    dairyAndAlternativesHeifaScoreMale = fields[42].toString(),
                    dairyAndAlternativesHeifaScoreFemale = fields[43].toString(),
                    dairyAndAlternativesServeSize = fields[44].toString(),
                    sodiumHeifaScoreMale = fields[45].toString(),
                    sodiumHeifaScoreFemale = fields[46].toString(),
                    sodiumMgMilligrams = fields[47].toString(),
                    alcoholHeifaScoreMale = fields[48].toString(),
                    alcoholHeifaScoreFemale = fields[49].toString(),
                    alcoholStandardDrinks = fields[50].toString(),
                    waterHeifaScoreMale = fields[51].toString(),
                    waterHeifaScoreFemale = fields[52].toString(),
                    water = fields[53].toString(),
                    waterTotalMl = fields[54].toString(),
                    beverageTotalMl = fields[55].toString(),
                    sugarHeifaScoreMale = fields[56].toString(),
                    sugarHeifaScoreFemale = fields[57].toString(),
                    sugar = fields[58].toString(),
                    saturatedFatHeifaScoreMale = fields[59].toString(),
                    saturatedFatHeifaScoreFemale = fields[60].toString(),
                    saturatedFat = fields[61].toString(),
                    unsaturatedFatHeifaScoreMale = fields[62].toString(),
                    unsaturatedFatHeifaScoreFemale = fields[63].toString(),
                    unsaturatedFatServeSize = fields[64].toString()
                )
                userDao.upsert(user)
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing line: $line", e)
            }
        }
    }
}


