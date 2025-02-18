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
    //Agreamos el binding
    private lateinit var binding: ActivityMainBinding

    //El listado de juegos que va a tener y la ponemos vacía
    private var games: List<GameEntity> = emptyList()

    //Para que nos instancie nuestro repositorio
    private lateinit var repository: GameRepository

    //Instanciamos el adapter
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Para tener view binding de cajon
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para instanciar el repositorio
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

        updateUI()
    }
//Creamos nuestra funcion para actualizar la UI,aqui se pone la Recicle View
    private fun updateUI() {
    //El alcance de vida de la app
    /*Alcance de ecorutina y le ponemos un ciclo de vida
    * cuando el main activity se destruya , se va a cancelar */
        lifecycleScope.launch {
            //Conseguimos el listado de juegos
            games = repository.getAllGames()

            //Si encontro un juego,por lo menos hay un registro
            if (games.isNotEmpty()) {
                //Hay por lo menos un registro,entonces la cajita de texto no se muestra
                //No hay registros !
                binding.tvSinRegistros.visibility = View.INVISIBLE
            } else {
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            //Aqui ponemos el gameAdapter y ya podemos ver el recycler view
            gameAdapter.updateList(games)
        }
    }
    //cremaos la funcion del main activity xml

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
        //Toast.makeText(this, "Click en el juego con id: ${game.id}", Toast.LENGTH_SHORT).show()
        //Se lo mandamos en falso ,para que se generen los botones con actualizar
        //El primer game se refiere al nombre del parametro y el siguiente game es el que me llega
        val dialog = GameDialog(newGame = false, game = game, updateUI = {
            //Le mandamos falso, para que nos muestre los botones desactivados
            //y mostramos la updateUI
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
