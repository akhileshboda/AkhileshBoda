package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing UserEntity data in Room.
 * Updated to use actual column names as defined in UserEntity.
 */
@Dao
interface UserDao {

    /**
     * Insert or update a single user. Replaces on conflict.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    /**
     * Set user password
     */
    @Query("UPDATE users SET Password = :password WHERE User_ID = :userId")
    suspend fun setPassword(userId: Int, password: String)

    /**
     * Get all users as a one-time suspend call.
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    /**
     * Observe all users as they change, ordered by User_ID.
     */
    @Query("SELECT * FROM users ORDER BY User_ID ASC")
    fun observeAllUsers(): Flow<List<UserEntity>>

    /**
     * Count total users in the table.
     */
    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int

    /**
     * Get a single user by its primary key (User_ID).
     */
    @Query("SELECT * FROM users WHERE User_ID = :userId")
    suspend fun getUserById(userId: Int): UserEntity

    /**
     * Delete a specific user row.
     */
    @Delete
    suspend fun delete(user: UserEntity)

    /**
     * Delete all users from the table.
     */
    @Query("DELETE FROM users")
    suspend fun deleteAll()

    /**
     * Search users by last name (partial match), using LastName column.
     */
    @Query("SELECT * FROM users WHERE LastName LIKE '%' || :lastName || '%' ORDER BY LastName ASC")
    fun findByLastName(lastName: String): Flow<List<UserEntity>>

    /**
     * Search users by first name (partial match), using FirstName column.
     */
    @Query("SELECT * FROM users WHERE FirstName LIKE '%' || :firstName || '%' ORDER BY FirstName ASC")
    fun findByFirstName(firstName: String): Flow<List<UserEntity>>

    /**
     * Query by phone number exact match, using PhoneNumber column.
     */
    @Query("SELECT * FROM users WHERE PhoneNumber = :phone")
    suspend fun getByPhoneNumber(phone: String): UserEntity?

    @Query("SELECT HEIFAtotalscoreMale  FROM users WHERE Sex = 'Male'")
    suspend fun getMaleHeifaScores(): List<String>

    @Query("SELECT HEIFAtotalscoreFemale  FROM users WHERE Sex = 'Female'")
    suspend fun getFemaleHeifaScores(): List<String>

}