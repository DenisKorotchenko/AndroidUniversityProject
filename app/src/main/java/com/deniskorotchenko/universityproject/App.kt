package com.deniskorotchenko.universityproject

import android.app.Application
import androidx.room.Room
import com.deniskorotchenko.universityproject.data.persistent.db.AppDB
import com.deniskorotchenko.universityproject.entity.Post
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import kotlin.concurrent.thread

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initDatabase()
    }

    private fun initDatabase() {
        val db = Room.databaseBuilder(
            this,
            AppDB::class.java, "app-database"
        )
            .allowMainThreadQueries()
            .build()

        db.postDao().insert(
            Post(
                id = 1,
                linkUrl = "linkUrl",
                imageUrl = "imageUrl",
                title = "title",
                text = "text",
                createdAt = "2021-01-01",
                updatedAt = "2021-01-10"
            )
        )
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}