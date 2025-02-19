package com.pjff.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameElementBinding


//Paso 1.11,Le pasamos un parámetro al Gameadapter y le pasamos una lambda
class GameAdapter(private val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>(){

    //Paso 1.13,Aquí estan los juegos que usara mi adapter
    private var games: List<GameEntity> = emptyList()

    /*
    Paso 1.12 el ViewHolder regresa un Recyclerview
    el binding  va hacia el GameElementBinding ,Son las que vamos a inflar
    */
    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        //Paso 1.16 Vinculamos el game entity , y los ligamos.
        fun bind(game: GameEntity){

            /*
              binding.tvTitle.text = game.title
              binding.tvGenre.text = game.genre
              binding.tvDeveloper.text = game.developer
            */

            /*
              Paso 1.17
              Es lo mismo que la parte de arriba
              con el binding.aply , es una función de alcance ,para ahorrar código.
            */
            binding.apply {
                //tvTitle , es como se llaman nuestras etiquetas
                tvTitle.text = game.title
                tvGenre.text = game.genre
                tvDeveloper.text = game.developer

            /*--------------------------------------------------------------------------------------
                              AQUI AGREGAREMOS CÓDIGO PARA LA PRACTICA 1
            --------------------------------------------------------------------------------------*/
            }
        }
    }

    //----------------------------------- Métodos del adapter --------------------------------------

    //Paso 1.15,Instanciamos un objeto del view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*Inflamos la vista de GameElementBinding, al ponerlo en falso no se va adjuntar automáticamente
        al view group del padre */
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //Paso 1.14,Le mandamos el numero de juegos y le retornomos el total de numeros (simplifica el codigo)
    override fun getItemCount(): Int = games.size

    //Paso 1.18
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Hacemos la vinculación.
        holder.bind(games[position])

        //Paso 1.19
        holder.itemView.setOnClickListener {
            /*
              Aquí va el click del elemento, le enviamos la lambda,para cuando instancie mi adapter
              sepa a cual jueguito se le hizo click
            */
            onGameClick(games[position])

        }

        holder.ivIcon.setOnClickListener {
            //Click para la vista del imageview con el ícono
        }

    }

    //Paso 1.20,Actualizamos el listado de datos
    fun updateList(list: List<GameEntity>){
        //Le pasamos el nuevo listado de la BD
        games = list
        //Le pondemos una notificación, esto no es muy óptimo de hacerlo,por eso marca warning
        notifyDataSetChanged()
    }

}