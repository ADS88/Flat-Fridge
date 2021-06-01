package com.example.flatfridge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flatfridge.data.FoodItem
import kotlinx.android.synthetic.main.fragment_add_food.*
import org.jetbrains.anko.toast
import java.io.File
import java.util.*


private const val FILE_NAME = "photo.jpg"
private const val REQUEST_IMAGE_CAPTURE = 1
private lateinit var photoFile: File

class AddFoodFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var imageView: ImageView
    lateinit var viewModel: AddFoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_add_food, container, false)

        val calendar = layout.findViewById<CalendarView>(R.id.calendarView)
        calendar.minDate = System.currentTimeMillis();


        viewModel = ViewModelProvider(this).get(AddFoodViewModel::class.java)

        if (viewModel.getExpiryDate() != null) {
            calendar.date = viewModel.getExpiryDate()!!
        }

        calendar.setOnDateChangeListener { view, year, month, day ->
            val dateSelected = Calendar.getInstance()
            dateSelected.set(year, month, day)
            val dateAsLong = dateSelected.timeInMillis
            viewModel.setExpiryDate(dateAsLong)
        }

        val imageButton = layout.findViewById<Button>(R.id.addImageButton)
        imageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        val foodNameEditText = layout.findViewById<EditText>(R.id.foodNameEditText)

        val addButton = layout.findViewById<Button>(R.id.addFoodButton)
        addButton.setOnClickListener {
            val photoFile = viewModel.getImageFile()
            val foodName = foodNameEditText.text
            if(foodName.isEmpty()) {
                activity?.toast(R.string.missing_name)
            } else if(photoFile == null){
                activity?.toast(R.string.missing_photo)
            }
            else {
                val expiryDate = if (viewModel.getExpiryDate() == null) calendar.date else viewModel.getExpiryDate()!!
                val foodItem = FoodItem(0, BitmapFactory.decodeFile(photoFile.absolutePath), foodName.toString(), expiryDate)
                viewModel.addFoodItem(foodItem)
                activity?.toast(R.string.successfully_added)
                resetFields()
            }
        }

        imageView = layout.findViewById(R.id.imageView)

        viewModel.getImageFile()?.let {file ->
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(takenImage)
        }



        return layout
    }



    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(this.requireContext(), "com.example.flatfridge.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {

        }
    }

    private fun resetFields(){
        foodNameEditText.text.clear()
        imageView.setImageBitmap(null)
        viewModel.setImageFile(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(takenImage )
            viewModel.setImageFile(photoFile)
        }
    }

    private fun getPhotoFile(fileName: String): File{
        val storageDirectory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

}