package com.pjff.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.util.Constants

//---------------------------- ES NUESTRA BASE DE DATOS -------------------------------------

//Paos 9, le ponemos su anotación.
@Database(
    /*En entitie que ya tenemos  donde estan las entidades
    Le pasamos las entidades con las que trabajaremos en nuestra base de datos.*/
    entities = [GameEntity::class],
    //versión de la BD. Importante para las migraciones
    version = 1,
    //por defecto esta en true
    exportSchema = true
)
//Paso 8,Para que funcione con room debe ser abstract class.
abstract class GameDatabase: RoomDatabase() {

    //Paso 21,Aquí va el DAO
    abstract fun gameDao(): GameDao

    /*Paso 22,Sin inyección de dependencias, metemos la creación de la BD con un singleton aquí.
    Con inyeccion de dependencias se usa providers.*/
    companion object{
        //-------------------------  Codigo Boiler play -------------------------------------

        /*Esto es código raro de google, que no le entendamos aún
        lo que se escriba en este campo, será inmediatamente visible a otros hilos*/
        @Volatile
        //Esto para que al momento de instanciar la base de datos haga la instancia.
        private var INSTANCE: GameDatabase? = null

        //Recibe el contexto de la aplicación de tipo GameDatabase
        fun getDatabase(context: Context): GameDatabase {
            //Nos regresa  una instancia , syn ,tiene que ver con los hilos
            return INSTANCE ?: synchronized(this){
                /*Si la instancia no es nula, entonces se regresa
                si es nula, entonces se crea la base de datos (patrón singleton)*/
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    //Le mandamos el nombre de nuestra base de datos
                    Constants.DATABASE_NAME
                //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran.
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                //regresamos instance
                instance
            }
        }
    }
}