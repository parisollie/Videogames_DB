package com.pjff.videogamesdb.data

import com.pjff.videogamesdb.data.db.GameDao
import com.pjff.videogamesdb.data.db.model.GameEntity

//Le ponemos nuestra inyeccion de dependencia:private val gameDao: GameDao
//Le ponemos el DAO ,porque de ahí lo leemos

class GameRepository(private val gameDao: GameDao) {

    //Funcion suspendida para poder insertar el juego
    //Requiere un objeto de la clase game entity,(game: GameEntity)
    suspend fun insertGame(game: GameEntity){
        gameDao.insertGame(game)
    }

    //Le pones funcion para pasarle estos parametros
    suspend fun insertGame(title: String, genre: String, developer: String){
        //Le pasamos un juego
        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------
        gameDao.insertGame(GameEntity(title = title, genre = genre, developer = developer))
    }
    //Regresame un list
    suspend fun getAllGames(): List<GameEntity> = gameDao.getAllGames()

    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: GameEntity){
        gameDao.deleteGame(game)
    }


}