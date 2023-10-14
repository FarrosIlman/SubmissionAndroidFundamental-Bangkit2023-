package com.parrosz.submissiongithubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.parrosz.submissiongithubuser.data.database.entity.UserEntity
import com.parrosz.submissiongithubuser.data.database.room.UserDao
import com.parrosz.submissiongithubuser.data.database.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = UserDatabase.getDatabase(application)
        userDao = database.userDao()
    }

    fun getAllFavorite(): LiveData<List<UserEntity>> = userDao.getAllFavoriteData()

    fun insertUser(user: UserEntity) {
        executorService.execute {
            userDao.insert((user))
        }
    }

    fun deleteUser(user: UserEntity) {
        executorService.execute {
            userDao.delete(user)
        }
    }

    fun getDataByUsername(username: String): LiveData<List<UserEntity>> = userDao.getDataByUsername(username)
}