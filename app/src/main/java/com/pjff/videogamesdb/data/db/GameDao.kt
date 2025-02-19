package com.pjff.videogamesdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.util.Constants.DATABASE_GAME_TABLE

//Paso 10, creamos el DAO
@Dao
interface GameDao {
    //---------------------------------------------------------------------------------

    //       Aqui definimos las operaciones CRUD : CREATE,READ, UPDATE,Y DELETE

    //---------------------------------------------------------------------------------

    //---------------------------------- CREATE ---------------------------------------
    @Insert
    /*Paso 11,Recibe un parametro Game entity
    Usamos una función suspendida, para que se ejecute sin afectar la aplicación
    La función se ejecuta en una corrutina*/
    suspend fun insertGame(game: GameEntity)

    //Paso 15,Le pasamos una lista de GameEntity en una listas sobrecargamos la funcion
    @Insert
    suspend fun insertGame(games: List<GameEntity>)

    //----------------------------------Read -----------------------------------------

    /*Paso 14

    Interpolamos(${DATABASE_GAME_TABLE}) y le decimos que de ahi viene.
    Pásame todos los datos de esta tabala ${DATABASE_GAME_TABLE}*/
    @Query("SELECT * FROM ${DATABASE_GAME_TABLE}")
    //regresame una lista de GameEntity
    suspend fun getAllGames(): List<GameEntity>

    //----------------------------------Update ---------------------------------------

    //Paso 12
    @Update
    suspend fun updateGame(game: GameEntity)

    //----------------------------------Delete  ---------------------------------------
    //Paso 13
    @Delete
    suspend fun deleteGame(game: GameEntity)
}