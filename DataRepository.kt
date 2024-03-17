package com.example.demoproject.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.demoproject.Model.Datum
import com.example.demoproject.Room.AppDatabase.Companion.getAppDatabase
import com.example.demoproject.Room.Dao.DataModelDao

class DataRepository(context: Context) {
    private val dataModelDao: DataModelDao?
    private val context: Context
    var LOG_TAG = "dataModelDao"
    var arrayListLiveData: LiveData<ArrayList<DataModelDao>>? = null

    init {
        val appDatabase = getAppDatabase(context)
        dataModelDao = appDatabase!!.dataModelDao()
        this.context = context
    }

    fun insertData(data: List<Datum?>?) {
        dataModelDao!!.insertData(data)
    }

    fun getData(): LiveData<List<Datum?>?>? {
        Log.e(LOG_TAG, "getData -------->")
        return dataModelDao!!.getData()
    }

    fun deleteList() {
        dataModelDao!!.deleteData()
    }
}
