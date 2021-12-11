package com.deniskorotchenko.universityproject.data.persistent.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.deniskorotchenko.universityproject.entity.Post

@Dao
interface PostDao {

    @Insert
    fun insert(post: Post)

    @Query("SELECT * FROM post")
    fun getAll(): List<Post>

    @Query("SELECT * FROM post WHERE id=:id")
    fun getById(id: Long): Post

    @Query("SELECT * FROM post WHERE text LIKE '%' || :textQuery || '%' OR title LIKE '%' || :textQuery || '%'")
    fun searchByTextQuery(textQuery: String): List<Post>

    @Delete
    fun delete(post: Post): Int
}