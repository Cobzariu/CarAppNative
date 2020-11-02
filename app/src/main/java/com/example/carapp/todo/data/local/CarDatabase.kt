package com.example.carapp.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carapp.todo.data.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Car::class], version = 1)
abstract class CarDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        //        @kotlinx.coroutines.InternalCoroutinesApi()
        fun getDatabase(context: Context, scope: CoroutineScope): CarDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.carDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(itemDao: CarDao) {
            itemDao.deleteAll()
        }
    }
}