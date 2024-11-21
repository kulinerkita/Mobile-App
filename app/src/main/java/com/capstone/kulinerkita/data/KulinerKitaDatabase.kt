package com.capstone.kulinerkita.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.kulinerkita.data.dao.UserDao
import com.capstone.kulinerkita.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class KulinerKitaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: KulinerKitaDatabase? = null

        fun getInstance(context: Context): KulinerKitaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KulinerKitaDatabase::class.java,
                    "kuliner_kita_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
