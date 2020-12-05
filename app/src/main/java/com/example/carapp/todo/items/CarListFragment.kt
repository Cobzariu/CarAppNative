package com.example.carapp.todo.items

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.carapp.R
import kotlinx.android.synthetic.main.fragment_item_list.*
import com.example.carapp.auth.data.AuthRepository
import com.example.carapp.core.Constants
import com.example.carapp.core.TAG
import kotlinx.android.synthetic.main.fragment_login.*

class CarListFragment() : Fragment() {
    private lateinit var itemListAdapter: CarListAdapter
    private lateinit var itemsModel: CarListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (Constants.instance()?.fetchValueString("token")==null) {
            findNavController().navigate(R.id.fragment_login)
            return;
        }
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.fragment_item_edit)
        }
        logout.setOnClickListener{
            Log.v(TAG, "LOGOUT")
            AuthRepository.logout()
            findNavController().navigate(R.id.fragment_login)
        }
        camera.setOnClickListener{
            findNavController().navigate(R.id.view_camera);
        }
        mapButton.setOnClickListener{
            findNavController().navigate(R.id.view_map);
        }
    }

    private fun setupItemList() {
        itemListAdapter = CarListAdapter(this)
        item_list.adapter = itemListAdapter
        itemsModel = ViewModelProvider(this).get(CarListViewModel::class.java)
        itemsModel.items.observe(viewLifecycleOwner, { items ->
            Log.v(TAG, "update items")
            itemListAdapter.items = items
        })
        itemsModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        itemsModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        itemsModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}