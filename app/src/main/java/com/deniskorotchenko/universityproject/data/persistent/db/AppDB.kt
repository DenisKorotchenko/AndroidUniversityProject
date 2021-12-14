package com.deniskorotchenko.universityproject.data.persistent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deniskorotchenko.universityproject.entity.Post

@Database(
    entities = [
        Post::class
    ],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract fun postDao(): PostDao
}