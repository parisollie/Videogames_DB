package com.pjff.videogamesdb.application

import android.app.Application
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.GameDatabase

//Hereda de application : y con su constructor ()
//Cuando usemos nuestra base de datos
//Instanciamos la base de datos y la usamos en donde queramos
class VideogamesDBApp(): Application() {
    /*La variable lazy ,me permite que se cree la instancia cuando yo la requiera y hasta ese momento
    se va emplear*/
    private val database by lazy{
        //Se usan cuando se requieren , le pasamos el contexto de nuestra aplicacion this@VideogamesDBApp
        GameDatabase.getDatabase(this@VideogamesDBApp)
    }

    //Como usamos un repositorio ,lo cargamos
    val repository by lazy{
        //Le pasamos el gamedao, ya que el game repository lo usa
        GameRepository(database.gameDao())
    }
}