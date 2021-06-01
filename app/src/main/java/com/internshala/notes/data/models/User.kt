package com.internshala.notes.data.models

import androidx.room.*
import com.google.gson.Gson

@Entity(
    tableName = "users",
    indices = [
        Index(unique = true, value = ["userId"])
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val name: String,
    val picUrl: String? = null,
    val email: String? = null,
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): User {
            val gson = Gson()
            return gson.fromJson(json, User::class.java)
        }
    }
}