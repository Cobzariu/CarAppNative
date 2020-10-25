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
import kotlinx.android.synthetic.main.fragment_item_edit.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ItemEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "_ID"
        const val HORSEPOWER="horsepower"
        const val AUTOMATIC="automatic"
        const val RELEASE_DATE="release_date"
    }

    private lateinit var viewModel: ItemEditViewModel
    private var itemId: String? = null
    private var horsepower: String? = null
    private var releaseDate: String? = null
    private var automatic: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
            if (it.containsKey(HORSEPOWER))
            {
                horsepower=it.getString(HORSEPOWER).toString();
            }
            if (it.containsKey(AUTOMATIC))
            {
                automatic=it.getBoolean(AUTOMATIC);
            }
            if (it.containsKey(RELEASE_DATE))
            {
                releaseDate=it.getString(RELEASE_DATE);
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
        car_name.setText(itemId)
        car_horsepower.setText(horsepower);
        car_automatic.isChecked = automatic;
        if (releaseDate!=null)
        {
            val date = LocalDate.parse(releaseDate);
            car_release_date.updateDate(date.year, date.monthValue, date.dayOfMonth)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val day: Int = car_release_date.dayOfMonth
            val month: Int = car_release_date.month + 1
            val year: Int = car_release_date.year
            val date=LocalDate.of(year, month, day)
            viewModel.saveOrUpdateItem(
                car_name.text.toString(),
                car_horsepower.text.toString().toInt(),
                car_automatic.isChecked,
                date.toString()
            )
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ItemEditViewModel::class.java)
        viewModel.item.observe(viewLifecycleOwner) { item ->
            Log.v(TAG, "update items")
            car_name.setText(item.name)
        }
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
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = itemId
        if (id != null) {
            viewModel.loadItem(id)
        }
    }
}
