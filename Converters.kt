package com.example.demoproject.Room.Convertor

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    var gson = Gson()
    @TypeConverter
    private fun stringToSomeObjectList(data: String?): List<DataModel> {
        if (data == null) {
            return emptyList<DataModel>()
        }
        val listType = object : TypeToken<List<DataModel?>?>() {}.type
        return gson.fromJson<List<DataModel>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<DataModel?>?): String {
        return gson.toJson(someObjects)
    }
}
