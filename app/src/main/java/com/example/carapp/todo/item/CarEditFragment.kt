package com.example.carapp.todo.item

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.carapp.R
import com.example.carapp.core.TAG
import com.example.carapp.todo.data.Car
import kotlinx.android.synthetic.main.fragment_item_edit.*
import java.time.LocalDate


class CarEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: CarEditViewModel
    private var itemId: String? = null
    private var car: Car? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val i = car
            if (i != null) {
                i.name = car_name.text.toString()
                i.automatic = car_automatic.isChecked
                i.horsepower = car_horsepower.text.toString()
                val day: Int = car_release_date.dayOfMonth
                val month: Int = car_release_date.month + 1
                val year: Int = car_release_date.year
                val date = LocalDate.of(year, month, day)
                i.releaseDate = date.toString()
                viewModel.saveOrUpdateItem(i)

            }
        }
        button_delete.setOnClickListener {
            if (car != null) {
                viewModel.deleteItem(car!!._id)
            }

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CarEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        }
        val id = itemId
        if (id == null) {
            car = Car("", "", "", "", false, "")
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner) {
                Log.v(TAG, "update items")
                if (it != null) {
                    car = it
                    car_name.setText(car!!.name)
                    car_horsepower.setText(car!!.horsepower)
                    car_automatic.isChecked = car!!.automatic
                    if (car!!.releaseDate.isNotEmpty()) {
                        val date = LocalDate.parse(car!!.releaseDate);
                        car_release_date.updateDate(date.year, date.monthValue, date.dayOfMonth)
                    }


                }
            }
        }
    }
}
