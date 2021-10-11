package com.example.foodapp.adapter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.FoodDialogFragment
import com.example.foodapp.databinding.FoodDialogFragmentBinding
import com.example.foodapp.databinding.FoodListItemBinding
import com.example.foodapp.model.Food

class FoodAdapter(private val context: Context): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private var list: List<Food> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                val price = "$$price"
                val origin = "Origin: $from"
                binding.foodNameTv.text = productName
                binding.foodOriginTv.text = origin
                binding.foodPriceTv.text = price

                Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(binding.foodImg)

                //show bottom dialog
                holder.itemView.setOnClickListener {
                    val foodDialogFragment = FoodDialogFragment.newInstance(this)
                    val activity = context as FragmentActivity
                    foodDialogFragment.show(activity.supportFragmentManager, "food_dialog_fragment")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //update list with new data
    fun updateList(list: List<Food>) {
        this.list = list
        notifyDataSetChanged()
    }

    class FoodViewHolder(var binding: FoodListItemBinding): RecyclerView.ViewHolder(binding.root)

}