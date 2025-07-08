package com.example.a1_akhilboda_34396268_fit2081.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository exposing user data operations to the rest of the app.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Insert or update a single user.
     */
    suspend fun upsert(user: UserEntity) = userDao.upsert(user)

    /**
     * Set user password
     */
    suspend fun setPassword(userId: Int, password: String) = userDao.setPassword(userId, password)

    /**
     * Fetch all users as a one-off list.
     */
    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()

    /**
     * Fetch a single user by its ID.
     */
    suspend fun getUserById(id: Int): UserEntity? = userDao.getUserById(id)

    /**
     * Delete a user.
     */
    suspend fun delete(user: UserEntity) = userDao.delete(user)

    suspend fun getHeifaScoresMale(): List<String> =
        userDao.getMaleHeifaScores()

    suspend fun getHeifaScoresFemale(): List<String> =
        userDao.getFemaleHeifaScores()


}
