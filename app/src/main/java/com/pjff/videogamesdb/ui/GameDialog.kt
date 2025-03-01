package com.pjff.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pjff.videogamesdb.R
import com.pjff.videogamesdb.application.VideogamesDBApp
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

//Paso 1.23, creamos el GameDialog
class GameDialog(
    //Paso 1.61
    private val newGame: Boolean = true,
    //Paso 1.44,Sino pasa nada tu generate tu propio game entity.
    private var game: GameEntity = GameEntity(
        /*
            Paso 1.43
           Cuando instancie un objeto le pasare un valor por defecto,
           para que al dar click no los enseñe y se pinte.

         -------------------------------------------------------------
                  AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
         -------------------------------------------------------------*/
        title = "",
        genre = "",
        developer = ""
    ),
    //Paso 1.55,Aquí recibo las lamdas ,como no recibe nada,la primera no regrese nada.
    private val updateUI: () -> Unit,
    //Paso 1.87,Le mandamos otra lambda para que se imprima el mensaje de si quieres eliminar el juego
    private val message: (String) -> Unit
) : DialogFragment() {

    //Paso 1.26,Para evitar fugas de memoria y google lo recomienda empezar en null.
    private var _binding: GameDialogBinding? = null

    //Paso 1.27,Para obtener cuando ya se haya instanciado el binding
    private val binding get() = _binding!!

    //Paso 1.30,Para generar el Alert dialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    //Paso 1.36,Cuando no se llenen los campos ,invalidamos el botón.
    private var saveButton: Button? = null

    //Paso 1.41,El repositorio es mi interacción con mi BD.
    private lateinit var repository: GameRepository

    //----------------------------------------------------------------------------------------------

    //Paso 1.23,Se configura el diálogo inicial.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Paso 1.29 ,Aqui inflamos el binding va ligado a un fragment o a un activity
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        //Paso 1.42,Instanciamos para el repositorio.
        repository = (requireContext().applicationContext as VideogamesDBApp).repository

        //Paso 1.31,Instanciamos,para ponerle mensajitos al usuario, la ventanita que sale.
        builder = AlertDialog.Builder(requireContext())
           //Paso 1.32, termina borrando todo esto
           // .setTitle("Juego")


        /*
           Paso 1.68,Aqui ponemos los valores con lo que llegan ,Damos por hecho que ya tiene algo
           binding.tietTitle.setText(game.title)
           binding.tietGenre.setText(game.genre)
           binding.tietDeveloper.setText(game.developer)
        */

        /*-------------------------------------------------------------
                  AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
         -------------------------------------------------------------*/

        //Paso 1.69,También lo podemos hacer con el apply.
        binding.apply {
            tietTitle.setText(game.title)
            tietGenre.setText(game.genre)
            tietDeveloper.setText(game.developer)
        }
        //paso 1.33.2,ponemos el dialogo, aunque se termina modificando todo
        //Paso 1.63,Si la banderita esta en true
        dialog = if (newGame) {
            buildDialog("Guardar", "Cancelar", {

                //Paso 1.74,Aqui le vamosa pasar las lambdas

                //**************** Create (Guardar) **********************

                /*-------------------------------------------------------------
                          AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                 -------------------------------------------------------------*/
                //Paso 1.45
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    //Paso 1.48,Aquí ya tenemos nuestra corutina
                    lifecycleScope.launch {
                        //Paso 1.47
                        repository.insertGame(game)
                    }
                    /*
                       Paso 1.49,El juego guardado es sin hacer hard coding
                       Toast.makeText(requireContext(),getString(R.string.juego_guardado),Toast.LENGTH_SHORT).show()
                       Toast.makeText(requireContext(),"Juego guardado correctamente",Toast.LENGTH_SHORT).show()
                    */
                    Toast.makeText(requireContext(),getString(R.string.juego_guardado),Toast.LENGTH_SHORT).show()

                    //Paso 1.55,Actualizar la UI
                    updateUI()

                } catch (e: IOException) {
                    //Paso 1.49,mandamos nuestra excepción.
                    e.printStackTrace()
                    //paso 1.88
                    message("Error al guardar el juego")
                }
            }, {
                //Paso 1.75,lambda Cancelar
            })
        } else {
            //Paso -1.64 Y sino lo que mostrará en los botones será esto
            buildDialog("Actualizar", "Borrar", {

                /*-------------------------------------------------------------
                         AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                 -------------------------------------------------------------*/

                //Paso 1.7,Les pasamos la lambda de Update

                //**************** Esta lambda es para el Update **********************
                //Paso 177, hacemos un copy paste
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    lifecycleScope.launch {
                        //Paso 1.78,Le pasamos el update y el juego
                        repository.updateGame(game)
                    }
                    //Paso 1.79,Esta harcodeado
                    message("Juego actualizado exitosamente")

                    /*
                      Paso 1.57 Actualizar la UI, para que cuando se agregue un juego se
                      ponte en el recycler view
                     */
                    updateUI()

                } catch (e: IOException) {
                    //Paso 1.80
                    e.printStackTrace()
                    message("Error al actualizar el juego")
                }

            }, {
                //**************** Paso 1.76 Esta lambda es para el Delete **********************

                //Paso 1.85,Le mandamos un dialogo al usuario para preguntarle ,si realmente quiere eliminar
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar") { _, _ ->
                        //Paso 1.81,Si el usuario pone que si
                        try {
                            lifecycleScope.launch {
                                //Paso 1.82,aqui solo le ponemos delete game ,a diferencia del update que es lo mismo
                                repository.deleteGame(game)
                            }

                            //Paso 1.83,Mensajes desde lambdas
                            message("Juego eliminado exitosamente")
                            //Paso 1.84,Actualizar la UI
                            updateUI()

                        } catch (e: IOException) {
                            e.printStackTrace()
                            message("Error al eliminar el juego")
                        }
                    }
                    //Paso 1.33.1, esto seria como el paso 1.33.1, pero lo termina modificando
                        //Paso 1.86,si el usuario dice que no
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        //Paso 1.34, regresamos el dialog
        return dialog
    }


    //Paso 1.24, Cuando se destruye el fragment.
    override fun onDestroy() {
        //Paso 1.28 ,para evitar memoy liks, lo pongo a nulo
        super.onDestroy()
        _binding = null
    }

    //Paso 1.25,Se llama después de que se muestra el diálogo en pantalla.
    override fun onStart() {
        super.onStart()

        /*Paso 1.37,el botón guardar estara deshabilitado
        Lo usamos para poder emplear el método getButton (ese no lo tiene el diálogo), lo casteamos*/
        val alertDialog = dialog as AlertDialog
        //Casteamos a falso
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        //Para que este deshabilitado, le ponemos (?) safe access, para que no haya problema
        saveButton?.isEnabled = false

        /*-------------------------------------------------------------
                 AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
         -------------------------------------------------------------*/

        //Paso 1.38,Cuando el usuario ingreso datos, tiene 3 miembros.
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

        //Paso 1.39,se repite como el de arriba,por que son 3 cajitas
        binding.tietGenre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        //Paso 1.40
        binding.tietDeveloper.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })


        /*-------------------------------------------------------------
                 AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
         -------------------------------------------------------------*/

    }

    //----------------------------------------------------------------------------------------------
               /*-------------------------------------------------------------
                          AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
               -------------------------------------------------------------*/

    //Paso 1.35,para cuando se validan los campos
    private fun validateFields() =

        /*
          Si ninguno esta vacío
          -------------------------------------------------------------
                AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
          -------------------------------------------------------------
         */
        (binding.tietTitle.text.toString().isNotEmpty() && binding.tietGenre.text.toString()
            .isNotEmpty() && binding.tietDeveloper.text.toString().isNotEmpty())


    /*1.62,Función para crear lo que quiero que tengan los botones del diálogo que se generen
    se hace de la misma forma que el de arriba, tienene la misma estructura*/
    private fun buildDialog(
        //le pasamos 4 parametros
        btn1Text: String,
        btn2Text: String,
        //Paso 1.70,Le pasamos dos lamdas con funciones
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Paso 1.71,Acción para el botón positivo
                positiveButton()
            })
            //_ esas cosas significa que no se usan ,como el de arriba nos marcan lineas amarillas
            .setNegativeButton(btn2Text) { _, _ ->
                //Paso 1.72,Acción para el botón negativo
                negativeButton()
            }
            .create()
}