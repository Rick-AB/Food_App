package com.example.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapter.FoodAdapter
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var foodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainActivityViewModel::class.java)
        observeData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        foodAdapter = FoodAdapter(this)
        val layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.foodRv.adapter = foodAdapter
        binding.foodRv.layoutManager = layoutManager
    }

    //observe changes to live data
    private fun observeData() {
        viewModel.init()
        viewModel.getFoodList().observe(this) {
            if (it.isNotEmpty() && it != null) {
                foodAdapter.updateList(it)
            }

        }

        viewModel.isLoading().observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.foodRv.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.foodRv.visibility = View.VISIBLE
            }
        }
    }
}