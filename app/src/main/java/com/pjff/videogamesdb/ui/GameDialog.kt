package com.pjff.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pjff.videogamesdb.R
import com.pjff.videogamesdb.application.VideogamesDBApp
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class GameDialog(
    //Cuando instancie un objeto le pasare un valor por defecto,para que al dar click
    //no los enseñe y se pinte
    private val newGame: Boolean = true,
    //Si no pasa nada tu generate tu propio game entity
    private var game: GameEntity = GameEntity(
        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------
        title = "",
        genre = "",
        developer = ""
    ),
    //Aqui recibi las lamdas , no recibe nada,la primera no regrese nada
    private val updateUI: () -> Unit,
    //Le mandamos otra lambda para que se imprima el mensaje de si quieres eliminar el juego
    private val message: (String) -> Unit
) : DialogFragment() {

    //Para evitar fugas de memoria
    private var _binding: GameDialogBinding? = null

    //para cuando instanciado el binding
    private val binding get() = _binding!!

    //para generar el Alert dialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    //Cuando no se llenen los campos ,invalidamos el boton
    private var saveButton: Button? = null

    //El repositorio es mi interaccion a mi base de datos
    private lateinit var repository: GameRepository

    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Aqui inflamos el binding va ligado a un fragment o a un activity
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        //Instanciamos para el repositorio
        repository = (requireContext().applicationContext as VideogamesDBApp).repository

        //Instanciamos,para ponerle mensajitos al usuario, la ventanita que sale
        builder = AlertDialog.Builder(requireContext())

        /*
        //Aqui ponemos los valores con lo que llegan ,Damos por hecho que ya tiene algo
        binding.tietTitle.setText(game.title)
        binding.tietGenre.setText(game.genre)
        binding.tietDeveloper.setText(game.developer)*/

        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------
        //Tambien lo podemos hacer con el apply
        binding.apply {

            tietTitle.setText(game.title)
            tietGenre.setText(game.genre)
            tietDeveloper.setText(game.developer)
        }
        //Si la banderia esta en true
        dialog = if (newGame) {
            buildDialog("Guardar", "Cancelar", {
                //Aqui le vamosa pasar las lambdas

                //**************** Create (Guardar) **********************

                //-------------------------------------------------------------
                // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                // -------------------------------------------------------------
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    //Aqui ya tenemos nuestra ecorutina
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                    //El juego guardado es sin hacer hard coding
                    //deberia hacerse así : R.string.juego_guardado y se guarda en strings
                    //message(getString(R.string.juego_guardado))
                    message(getString(R.string.juego_guardado))

                    //Actualizar la UI
                    updateUI()

                } catch (e: IOException) {
                    e.printStackTrace()
                    message("Error al guardar el juego")
                }
            }, {
                //Cancelar
            })
        } else {
            //Y sino lo que mostrara en los botones sera esto
            buildDialog("Actualizar", "Borrar", {
                //-------------------------------------------------------------
                // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                // -------------------------------------------------------------
                //Les pasamos las lamdas

                //**************** Esta lambda es para el Update **********************

                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    lifecycleScope.launch {
                        //Le pasamos el update
                        repository.updateGame(game)
                    }
                    //Esta harcodeado
                    message("Juego actualizado exitosamente")

                    //Actualizar la UI, para que cuando se agregue un juego se ponte en el recycler view
                    updateUI()

                } catch (e: IOException) {
                    e.printStackTrace()
                    message("Error al actualizar el juego")
                }

            }, {
                //**************** Esta lambda es para el Delete **********************

                //Le mandamos un dialogo al usuario para preguntarle ,si realmente quiere eliminar
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar") { _, _ ->
                        //Si el usuario pone que si
                        try {
                            lifecycleScope.launch {
                                //aqui solo le ponemos delete game ,a diferencia del update que es lo mismo
                                repository.deleteGame(game)
                            }

                            //Mensajes desde lambdas
                            message("Juego eliminado exitosamente")
                            //Actualizar la UI
                            updateUI()

                        } catch (e: IOException) {
                            e.printStackTrace()
                            message("Error al eliminar el juego")
                        }
                    }
                        //si el usuario dice que no
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        //Le pasamos la lista
        /*dialog = builder.setView(binding.root)
        //El titulo que quiero que tenga el juego
            .setTitle("Juego")
            //Cuando el usuario acepta cuestiones
            //_,_ , significa que esos parametros no los usa
            .setPositiveButton("Guardar", DialogInterface.OnClickListener { _, _ ->
                //Guardar, cuando las cajas ya tienen algo
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                //le mandamos una ecorutina
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                //Si algo paso mal
                //aqui ya esta sin harcodear
                    Toast.makeText(requireContext(), getString(R.string.juego_guardado), Toast.LENGTH_SHORT).show()
                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error al guardar el juego", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { _, _ ->

            })
            .create()*/

        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        //para evitar memoy liks
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog =
            dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)//Casteamos a falsp
        //para que este deshabilitado
        saveButton?.isEnabled = false
        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------

        //Cuando el usuario ingreso datos
        binding.tietTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                //Cuando el usario cambio el texto en la cajita
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietGenre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietDeveloper.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------

    }

    //-------------------------------------------------------------
    // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1-Validacion
    // -------------------------------------------------------------

    //para cuando se validan los campos
    private fun validateFields() =
        //Si ninguno esta vacio
    //-------------------------------------------------------------
    // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------
        (binding.tietTitle.text.toString().isNotEmpty() && binding.tietGenre.text.toString()
            .isNotEmpty() && binding.tietDeveloper.text.toString().isNotEmpty())

    //Funcion para crear lo que quiero que tengan los botones del dialogo que se generen
    private fun buildDialog(
        //le pasamos 4 parametros
        btn1Text: String,
        btn2Text: String,
        //Le pasamos dos lamdas con funciones
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()
}