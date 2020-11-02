package com.example.carapp.todo.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.carapp.R
import kotlinx.android.synthetic.main.view_item.view.*
import com.example.carapp.core.TAG
import com.example.carapp.todo.data.Car
import com.example.carapp.todo.item.CarEditFragment

class CarListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    var items = emptyList<Car>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Car
            fragment.findNavController().navigate(R.id.fragment_item_edit, Bundle().apply {
                putString(CarEditFragment.ITEM_ID, item._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.textView.text = item.name
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
