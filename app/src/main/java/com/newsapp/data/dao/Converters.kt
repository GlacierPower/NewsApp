package com.newsapp.data.dao

import androidx.room.TypeConverter
import com.newsapp.data.data_base.Source

class Converters {
    @TypeConverter
    fun fromSource(s: Source):String = s.name

    @TypeConverter
    fun toSource(name:String): Source = Source(name,name)
}