package com.furkanterzi.recycleview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkanterzi.recycleview.databinding.RecyclerRowBinding

class SuperHeroAdapter(val superHeroList: ArrayList<SuperHero>): RecyclerView.Adapter<SuperHeroAdapter.SuperHeroViewHolder>() {
    class SuperHeroViewHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperHeroViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return superHeroList.size
    }

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        holder.binding.textViewRecyclerView.text = superHeroList[position].name

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            //MySingleton.pickedHero = superHeroList[position]
            intent.putExtra("pickedHero", superHeroList[position])
            holder.itemView.context.startActivity(intent)
        }
    }
}