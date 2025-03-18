package com.furkanterzi.recycleview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkanterzi.recycleview.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*
        val pickedHero = intent.getSerializableExtra("pickedHero", SuperHero::class.java)
        val pickedHero = MySingleton.pickedHero
        pickedHero.let {
           binding.imageView.setImageResource(pickedHero.image)
            binding.textView.text = pickedHero.name
            binding.textView2.text = pickedHero.job
        }
        */
        val pickedHero = intent.getSerializableExtra("pickedHero") as SuperHero
        binding.imageView.setImageResource(pickedHero.image)
        binding.textView.text = pickedHero.name
        binding.textView2.text = pickedHero.job

    }
}