package com.furkanterzi.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkanterzi.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
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

        sharedPreferences = getSharedPreferences("com.furkanterzi.sharedpreferences", Context.MODE_PRIVATE)

        binding.textView.text = sharedPreferences.getString("name","")


    }
    fun save(view: View){
        if (binding.editText.text.toString() == ""){
            Toast.makeText(this@MainActivity, "name is required ", Toast.LENGTH_SHORT).show()
        }else{
            sharedPreferences.edit().putString("name", binding.editText.text.toString()).apply()
        }
    }
    fun delete(view: View){
        sharedPreferences.edit().remove("name").apply()
        binding.textView.text = "Shared Prefences"
    }
}