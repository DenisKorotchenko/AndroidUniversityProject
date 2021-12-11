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
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}