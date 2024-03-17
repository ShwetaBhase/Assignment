package com.example.demoproject.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.demoproject.Model.DataModel
import com.example.demoproject.Room.Convertor.Converters
import com.example.demoproject.Room.Dao.DataModelDao

@Database(entities = [DataModel::class], version = 1, exportSchema = false)
@TypeConverters(*[Converters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataModelDao(): DataModelDao?

    companion object {
        private var INSTANCE: AppDatabase? = null
        @JvmStatic
        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).build()
            }
            return INSTANCE
        }
    }
}
