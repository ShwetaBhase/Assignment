package com.example.demoproject.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demoproject.Model.Datum

@Dao
interface DataModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: List<Datum?>?)

    @get:Query("SELECT * from table_item_master")
    val customerList: LiveData<List<Datum?>?>?

    @Query("DELETE FROM table_item_master")
    fun deleteData()

    @Query("select * from table_item_master")
    fun getData(): LiveData<List<Datum?>?>?
}
