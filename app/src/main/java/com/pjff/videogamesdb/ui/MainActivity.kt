package com.pjff.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pjff.videogamesdb.R
import com.pjff.videogamesdb.application.VideogamesDBApp
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //Paso 1.0 ,Agregamos el binding
    private lateinit var binding: ActivityMainBinding

    //Paso 1.2,El listado de juegos que va a tener y la ponemos una lista vacía
    private var games: List<GameEntity> = emptyList()

    //Paso 1.3, Para que nos instancie nuestro repositorio
    private lateinit var repository: GameRepository

    //Instanciamos el adapter
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Paso 1.1,Para tener view binding de cajón
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Paso 1.4,Para instanciar el repositorio
        repository = (application as VideogamesDBApp).repository

        //Instanciamos el GameAdapter ,pero le pasamos la funcion Lambda del Game Dialog
        gameAdapter = GameAdapter(){ game ->
            //Le pasamos la funcion gameClicked
            gameClicked(game)
        }

        //binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
        //binding.rvGames.adapter = gameAdapter

        //Instanciamos el game adapter
        //gameAdapter =GameAdapter ()

        binding.rvGames.apply {
            //Mandamos a llamar el RecyclerView
            layoutManager = LinearLayoutManager(this@MainActivity)
            //Le ponemos el adapater
            adapter = gameAdapter
        }
        //Paso 1.9, se ejecuta la funcion
        updateUI()
    }
//Paso 1.5 ,Creamos nuestra función para actualizar la UI,aqui se pone la Recicle View.
    private fun updateUI() {
        /*
        Paso 1.6

        El alcance de ciclo de vida de la app : lifecycleScope
        Alcance de corutina y le ponemos un ciclo de vida,entonces,
        cuando el main activity se destruya , en el ciclo de vida esta se va a cancelar.
        */
        lifecycleScope.launch {
            /*Paso 1.7,Conseguimos el listado de juegos, getAll Games es un funcion suspendida
            en la parte izquierda de la pantalla vemos una flecha que significa eso*/
            games = repository.getAllGames()

            //Paso 1.8,Si encontró un juego,por lo menos hay un registro
            if (games.isNotEmpty()) {
                /*Hay por lo menos un registro,entonces la cajita de texto no se muestra
                No hay registros -> !*/
                binding.tvSinRegistros.visibility = View.INVISIBLE
            } else {
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            //Aqui ponemos el gameAdapter y ya podemos ver el recycler view
            gameAdapter.updateList(games)
        }
    }


    //Creamos la funcion del main activity XML
    fun click(view: View) {
        //Aqui le estamos pasando las lambdas
        val dialog = GameDialog( updateUI = {
            //Le pasamos la lambda UpdateUi  de GameDialogy la lambda del mensaje
            updateUI()
        }, message = { text ->
            message(text)
       })
        //La etiqueta dialog es para mostrarlo en pantalla
        dialog.show(supportFragmentManager, "dialog")
    }

    //Cuando le hacen click al juego
    private fun gameClicked(game: GameEntity){
        /*

        Toast.makeText(this, "Click en el juego con id: ${game.id}", Toast.LENGTH_SHORT).show()
        Se lo mandamos en falso ,para que se generen los botones con actualizar
        El primer game se refiere al nombre del parametro y el siguiente game es el que me llega

        */
        val dialog = GameDialog(newGame = false, game = game, updateUI = {
            /*Le mandamos falso, para que nos muestre los botones desactivados
            y mostramos la updateUI*/
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            //esta harcodeado
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}
