package com.example.foodapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.FoodDialogFragmentBinding
import com.example.foodapp.model.Food
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// represents popup dialog to show food details
class FoodDialogFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FoodDialogFragmentBinding
    private lateinit var food: Food
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoodDialogFragmentBinding.inflate(inflater, container, false)
        food = arguments?.getSerializable("food") as Food
        updateViews()
        return binding.root
    }
    //updates view with selected food item
    private fun updateViews() {
        val price = "$${food.price}"
        val organic = if (food.organic) "Organic product" else "Inorganic product"
        val nutrients = "Contains: ${food.nutrients}"
        val quantity = "Qty: ${food.quantity}"
        val origin = "Origin: ${food.from}"

        binding.dialogFoodNameTv.text = food.productName
        binding.dialogFoodPriceTv.text = price
        binding.dialogFoodOriginTv.text = origin
        binding.dialogFoodQty.text = quantity
        binding.dialogFoodOrganic.text = organic
        binding.dialogFoodNutrients.text = nutrients
        binding.dialogFoodDesc.text = food.description

        Glide.with(requireContext())
            .load(food.imageUrl)
            .centerCrop()
            .into(binding.dialogFoodImg)
    }

    companion object {
        fun newInstance(food: Food): FoodDialogFragment {
            val fragment = FoodDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable("food", food)
            fragment.arguments = bundle
            return fragment
        }
    }
}