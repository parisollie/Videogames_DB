package com.pjff.videogamesdb.application

import android.app.Application
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.GameDatabase

//Paso 2, creamos class VideogamesDBApp(): Application() con los (:)
class VideogamesDBApp(): Application() {
    /*Paso 23

    -Cuando usemos nuestra base de datos ,instanciamos la base de datos y la usamos en donde queramos
    -La variable lazy ,me permite que se cree la instancia cuando yo la requiera y hasta ese momento
    se va emplear

    */
    private val database by lazy{
        //Paso 24,Le pasamos el contexto de nuestra aplicación (this@VideogamesDBApp)
        GameDatabase.getDatabase(this@VideogamesDBApp)
    }

    //Paso 25,Como usamos un repositorio ,lo cargamos
    val repository by lazy{
        //Le pasamos el gameDao, ya que el GameRepository lo usa
        GameRepository(database.gameDao())
    }
}