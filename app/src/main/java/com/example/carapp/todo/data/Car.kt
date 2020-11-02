package com.example.carapp.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "horsepower") var horsepower: String,
    @ColumnInfo(name = "automatic") var automatic: Boolean,
    @ColumnInfo(name = "releaseDate") var releaseDate: String

) {
    override fun toString(): String = name
}
