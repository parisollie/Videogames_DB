package com.pjff.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pjff.videogamesdb.util.Constants

//Le pasamos las tablas y los datos a capturar
@Entity(tableName = Constants.DATABASE_GAME_TABLE)//así yo quiero que se llame mi tabla y no como
//Game entity
data class GameEntity(

    //Con Room le ponemos el primaryKey
    @PrimaryKey(autoGenerate = true)
    //Le decimos que se guarde como game_id
    @ColumnInfo(name = "game_id")
    //------------------- LOS CAMPOS DE NUESTRA ENTIDAD -------------------------------

    //Este id quiero que se use como llave primaria,empieza en 0,porque se va autoincrementar
    val id: Long = 0,
    //Clolumn info significa que es nuestra columna
    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genre")
    var genre: String,

    @ColumnInfo(name = "game_developer")
    var developer: String

)
