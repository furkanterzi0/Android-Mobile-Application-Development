package com.furkanterzi.roomanddatabase.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.furkanterzi.roomanddatabase.databinding.FragmentRecipeBinding
import com.furkanterzi.roomanddatabase.model.Recipe
import com.furkanterzi.roomanddatabase.roomdb.RecipeDAO
import com.furkanterzi.roomanddatabase.roomdb.RecipeDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImage: Uri? = null
    private var selectedBitmap: Bitmap? = null

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
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerLauncher()
        arguments?.let {
            val info = RecipeFragmentArgs.fromBundle(it).info
            val id = RecipeFragmentArgs.fromBundle(it).id

            if(info.equals("new")){
                binding.deleteButton.isEnabled = false
                binding.saveButton.isEnabled = true
                binding.nameText.setText("")
                binding.detailText.setText("")
            }else{
                binding.deleteButton.isEnabled = true
                binding.saveButton.isEnabled = false
                val id = RecipeFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    recipeDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )
            }
        }

        binding.imageView.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)) // reddedildi ise
                    //snackbar göstermemiz lazım, neden izin istediğimizi bir kez daha söyleyerek.
                        Snackbar.make(it,"We need access to your gallery",Snackbar.LENGTH_INDEFINITE).
                        setAction("Allow",
                            {
                                //izin istenecek
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                            }).show()
                    else{
                        // daha önce reddedilmediyse
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }

                }else{
                    val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }
            else{
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) // reddedildi ise
                    //snackbar göstermemiz lazım, neden izin istediğimizi bir kez daha söyleyerek.
                        Snackbar.make(it,"We need access to your gallery",Snackbar.LENGTH_INDEFINITE).
                        setAction("Allow",
                            {
                                //izin istenecek
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                            }).show()
                    else{
                        // daha önce reddedilmediyse
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }

                }else{
                    val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }
        }

    }
    private fun handleResponse(recipe: Recipe){
        binding.nameText.setText(recipe.name)
        binding.detailText.setText(recipe.foodDetail)
        val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0,recipe.image.size)
        binding.imageView.setImageBitmap(bitmap)
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selectedImage = intentFromResult.data

                    try {
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedImage!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }else{
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }
                    }catch (e: Exception){
                        println(e.localizedMessage)
                    }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if(result){
                //izin verildi, galeriye gidebiliriz
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //izin verilmedi
                Toast.makeText(requireContext(), "not allowed", Toast.LENGTH_LONG).show()
            }
        }
        binding?.saveButton?.setOnClickListener {
            val name = binding.nameText.toString()
            val detail = binding.detailText.toString()

            if(selectedBitmap != null){
                val smallBitmap= createSmallBitmap(selectedBitmap!!, 300)
                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                val byteArray = outputStream.toByteArray()

                val recipe = Recipe(name,detail,byteArray)

                // RxJava
                mDisposable.add(
                    recipeDao.insert(recipe)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers
                        .mainThread())
                        .subscribe(this::handleResponseForInsert)
                )
            }
        }
    }
    private fun handleResponseForInsert(){
        val action = RecipeFragmentDirections.actionRecipeFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    private fun createSmallBitmap(selectedBitmap: Bitmap, maxSize: Int): Bitmap {
        var width = selectedBitmap.width
        var height = selectedBitmap.height

        val aspectRatio: Double = width.toDouble() / height.toDouble()

        if (aspectRatio > 1) {
            width = maxSize
            val scaledHeight = width / aspectRatio
            height = scaledHeight.toInt()
        } else {
            height = maxSize
            val scaledWidth = height * aspectRatio
            width = scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(selectedBitmap, width, height, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}