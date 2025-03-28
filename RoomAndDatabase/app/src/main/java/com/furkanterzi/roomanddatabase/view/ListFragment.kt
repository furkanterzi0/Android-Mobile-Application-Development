package com.furkanterzi.roomanddatabase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.furkanterzi.roomanddatabase.adapter.RecipeAdapter
import com.furkanterzi.roomanddatabase.databinding.FragmentListBinding
import com.furkanterzi.roomanddatabase.model.Recipe
import com.furkanterzi.roomanddatabase.roomdb.RecipeDAO
import com.furkanterzi.roomanddatabase.roomdb.RecipeDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: RecipeDatabase
    private lateinit var recipeDao: RecipeDAO

    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(), RecipeDatabase::class.java, "Recipes").build()
        recipeDao = db.recipeDAO()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToRecipeFragment(info = "new", id = 1)
            Navigation.findNavController(it).navigate(action)
        }
        binding.recipeListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getRecipes()
    }
    private fun getRecipes(){
        mDisposable.add(
            recipeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(recipes: List<Recipe>){
        val adapter = RecipeAdapter(recipes)
        binding.recipeListRecyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}