package com.example.a1_akhilboda_34396268_fit2081.model.repositories

import com.example.a1_akhilboda_34396268_fit2081.model.daos.UserDao
import com.example.a1_akhilboda_34396268_fit2081.model.entities.UserEntity
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
     * Observe all users as they change.
     */
    fun observeAllUsers(): Flow<List<UserEntity>> = userDao.observeAllUsers()

    /**
     * Fetch a single user by its ID.
     */
    suspend fun getUserById(id: Int): UserEntity? = userDao.getUserById(id)

    /**
     * Delete a user.
     */
    suspend fun delete(user: UserEntity) = userDao.delete(user)

    /**
     * Delete all users from the database.
     */
    suspend fun deleteAll() = userDao.deleteAll()
}