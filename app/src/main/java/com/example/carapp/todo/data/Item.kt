package com.example.carapp.todo.data

data class Item(
    val _id: String,
    val userId: String,
    var name: String,
    var horsepower: Number,
    var automatic: Boolean,
    var releaseDate: String

) {
    override fun toString(): String = name
}
