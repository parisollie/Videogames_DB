package com.pjff.videogamesdb.data

import com.pjff.videogamesdb.data.db.GameDao
import com.pjff.videogamesdb.data.db.model.GameEntity

/*
Paso 16, creamos el GameRepository.

Le ponemos nuestra inyección de dependencia -> (private val gameDao: GameDao)
Le ponemos el DAO ,porque desde  ahí lo leemos*/

class GameRepository(private val gameDao: GameDao) {

    /*Paos 17,Función suspendida para poder insertar el juego
    Requiere un objeto de la clase game entity,(game: GameEntity)*/
    suspend fun insertGame(game: GameEntity){
        //Ejecuta el insertGame del DAO
        gameDao.insertGame(game)
    }

    //Le pones función para pasarle estos parámetros
    suspend fun insertGame(title: String, genre: String, developer: String){
        /*Le pasamos un juego
        -------------------------------------------------------------
          AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        -------------------------------------------------------------*/
        gameDao.insertGame(GameEntity(title = title, genre = genre, developer = developer))
    }

    //Paso 20,Regresame un list, simplifica el return con esto -> : List<GameEntity>
    suspend fun getAllGames(): List<GameEntity> = gameDao.getAllGames()

    //Paso 18 funcion para actualizar
    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }

    //Paso 19, funcion para eliminar
    suspend fun deleteGame(game: GameEntity){
        gameDao.deleteGame(game)
    }


}