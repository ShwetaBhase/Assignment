package com.example.demoproject.Viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.demoproject.Model.DataModel
import com.example.demoproject.Model.DataModel.Datum
import com.example.demoproject.Model.Datum

import com.example.demoproject.Repository.DataRepository

class DataViewModel(application: Application) : AndroidViewModel(application) {
    var dataRepository: DataRepository
    var LOG_TAG = "DataViewModel"
    var dataList: ArrayList<Datum>

    init {
        dataRepository = DataRepository(application)
        dataList = ArrayList()
    }

    fun insertData(data: List<Datum?>?) {
        dataRepository.insertData(data)
    }

    fun getDataList(): LiveData<List<Datum?>?>? {
        return dataRepository.getData()
    }

    fun deleteData() {
        dataRepository.deleteList()

    }
}
