package com.pjff.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameElementBinding


//Le pasamos un parametro al Gameadapter y le pasamos una lambda
class GameAdapter(private val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>(){

    //Aqui estan los juegos que usara mi adapter
    private var games: List<GameEntity> = emptyList()

    //el binding  va hacia el GameElementBinding ,Son las que vamos a inflar
    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        //Vinculamos el game entity , y los ligamos
        fun bind(game: GameEntity){
            /*binding.tvTitle.text = game.title
            binding.tvGenre.text = game.genre
            binding.tvDeveloper.text = game.developer*/

            //Es lo mismo que la parte de arriba
            /* con el binding.aply , es una funcion de alcance ,para ahorarrr codigo */
            binding.apply {
                //tvTitle , es como se llaman nuestras etiquetas
                tvTitle.text = game.title
                tvGenre.text = game.genre
                tvDeveloper.text = game.developer
            //-------------------------------------------------------------
            // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
            // -------------------------------------------------------------
            }
        }
    }

    //Instanciamos un objeto del view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //Le mandamos el numero de juegos y le retornomos el total de numeros (simplifica el codigo)
    override fun getItemCount(): Int = games.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])

        holder.itemView.setOnClickListener {
            //Aquí va el click del elemento, le enviamos la lambda,para cuando instancie mi adapter
            //sepa a cual jueguito se le hizo click
            onGameClick(games[position])

        }

        holder.ivIcon.setOnClickListener {
            //Click para la vista del imageview con el ícono
        }

    }

    //Actualizamos el listado de datos
    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()
    }

}