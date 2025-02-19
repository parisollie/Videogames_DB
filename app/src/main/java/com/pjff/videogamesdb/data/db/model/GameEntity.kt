package com.pjff.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pjff.videogamesdb.util.Constants

/*Le pasamos la etiqueta entity , al entity le pasa una tamableName
y el nombre de la tabla de nuestro archivo de Constants.*/
@Entity(tableName = Constants.DATABASE_GAME_TABLE)
//Paso 6, creamos nuestra entidad Game entity
data class GameEntity(

    //------------------- LOS CAMPOS DE NUESTRA ENTIDAD -------------------------------
    //Paso 7, pongo mis varibales

    //Con Room le ponemos el primaryKey
    @PrimaryKey(autoGenerate = true)
    //Le decimos que se guarde como game_id
    @ColumnInfo(name = "game_id")
    /*Este id quiero que se use como llave primaria, empieza en 0,
    porque se va autoincrementar*/
    val id: Long = 0,

    //ColumnInfo -> significa que es nuestra columna
    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genre")
    var genre: String,

    @ColumnInfo(name = "game_developer")
    var developer: String

)
