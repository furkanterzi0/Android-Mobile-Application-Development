package com.furkanterzi.myapplication2life_cycle

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkanterzi.myapplication2life_cycle.databinding.ActivityMainBinding
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
        println("onCrate calisti")
    }

    override fun onStart() {
        super.onStart()
        println("onStart calisti")
    }

    override fun onResume() {
        super.onResume()
        println("onResume calisti")
    }

    override fun onPause() {
        super.onPause()
        println("onPause calisti")
    }

    override fun onStop() {
        super.onStop()
        println("onStop calisti")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy calisti")
    }
    fun nextPage(view: View){

        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("username", binding.editText.text.toString())
        startActivity(intent)

    }


}