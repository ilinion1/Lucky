package com.example.portfolio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.portfolio.databinding.Activity2Binding
import com.example.portfolio.databinding.ActivityConditionsBinding

class ActivityConditions : AppCompatActivity() {
    lateinit var binding: ActivityConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //закрываю активити
        binding.bBack.setOnClickListener {
            finish()
        }
    }
}