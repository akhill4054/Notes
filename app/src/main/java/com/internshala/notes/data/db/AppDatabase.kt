package com.internshala.notes.data.db

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.internshala.notes.data.db.dao.NoteDao
import com.internshala.notes.data.db.dao.UserDao
import com.internshala.notes.data.models.Note
import com.internshala.notes.data.models.User

@Database(entities = [User::class, Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: Application): AppDatabase {
            var localRef = INSTANCE
            return localRef ?: synchronized(AppDatabase::class.java) {
                localRef = INSTANCE

                return if (localRef != null) {
                    localRef!!
                } else {
                    val db = Room.databaseBuilder(
                        application,
                        AppDatabase::class.java, "notes-app-db"
                    ).fallbackToDestructiveMigration().build()

                    // Init static field
                    INSTANCE = db
                    db
                }
            }
        }
    }
}