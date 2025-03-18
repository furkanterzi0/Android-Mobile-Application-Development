package com.furkanterzi.recycleview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkanterzi.recycleview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var superHeroList: ArrayList<SuperHero>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ironman = SuperHero("Iron Man", "Genius, Billionaire, Playboy, Philanthropist", R.drawable.ironman)
        val batman = SuperHero("Batman", "Vigilante, Billionaire", R.drawable.batman)
        val hulk = SuperHero("Hulk", "Scientist, Avenger", R.drawable.hulk)
        val aquaman = SuperHero("Aquaman", "King of Atlantis", R.drawable.aquaman)

        superHeroList = arrayListOf(ironman,batman,hulk,aquaman)

        val adapter = SuperHeroAdapter(superHeroList)
        binding.superHeroRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.superHeroRecyclerView.adapter = adapter
    }

}