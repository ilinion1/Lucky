package com.example.portfolio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.portfolio.databinding.ActivitySettingBinding


class ActivitySetting : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //прячет имя если его не указывали
        if(binding.edName.text.isNullOrEmpty() && binding.edName2.text.isNullOrEmpty() ){
            binding.tvName.visibility = View.GONE
        }

        //закрываю активити и передаю имя и фамилию, так же задаю в настройках имя
        binding.bBack.setOnClickListener {
            if (binding.edName.text.isNullOrEmpty()) {
                finish()
            } else{

              val name = binding.edName.text.toString()  + binding.edName2.text.toString()
              val i = Intent()
                i.putExtra("key","$name")
                val adress = binding.edAddress.text.toString()
                i.putExtra("key2","$adress")
                setResult(RESULT_OK, i)
                binding.tvName.visibility = View.VISIBLE
                binding.tvName.text = name
                finish()
            }
        }

    }


}