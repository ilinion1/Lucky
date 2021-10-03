package com.example.portfolio

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.portfolio.databinding.Activity2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class Activity2 : AppCompatActivity() {
    lateinit var binding: Activity2Binding
    private val dataModel: DataModel by viewModels()
    private var launcher: ActivityResultLauncher<Intent>? = null
    lateinit var auth: FirebaseAuth
    val imageList = arrayListOf<Int>(
        R.drawable.chaynik, R.drawable.fen, R.drawable.noutbuk, R.drawable.powerbank,
        R.drawable.pristavka, R.drawable.proig1, R.drawable.proig2, R.drawable.proig3,
        R.drawable.proig4, R.drawable.pulesos, R.drawable.samokat, R.drawable.telefon,
        R.drawable.utug, R.drawable.proig5, R.drawable.proig6, R.drawable.proig7,
        R.drawable.proig8,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        //наблюдает когда можно обновить данные полученные с фрагмента
        dataModel.message.observe(this,{
            binding.tvCash.text = it
            if (binding.tvCash.text != "0") {
                binding.tvCash.setTextColor(Color.GREEN)
                binding.bRepenish.visibility = View.VISIBLE
                binding.placeHolder.visibility = View.GONE
            }

        })

        //ожидаю результат с активити настройки
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            if (result.resultCode == RESULT_OK){
                val cashSet = result.data?.getStringExtra("key")
                binding.tvNameDrawer.visibility = View.VISIBLE
                binding.tvNameDrawer.text = cashSet
            }
            if (result.resultCode== RESULT_OK){
                val adress = result.data?.getStringExtra("key2")
                binding.tvAdress.text = adress
            }
        }

        //открывает активити по клику в меню
        binding.bNav.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.conditions ->{
                    //открываю активи conditions
                    val i = Intent(this, ActivityConditions::class.java)
                    startActivity(i)
                }
                R.id.setting ->{
                    //открываю активи setting  с ожиданием результата
                    launcher?.launch(Intent(this,ActivitySetting::class.java))
                }
            }
            true
            }

        //меняю картинки призов кнопкой "еще призы"
        var count=0
        binding.bPrize.setOnClickListener {
            if (count==0){
            binding.im1.setImageResource(R.drawable.samokat)
            binding.im2.setImageResource(R.drawable.fen)
            binding.im3.setImageResource(R.drawable.telefon)
               count++
            } else if (count==1) {
                binding.im1.setImageResource(R.drawable.noutbuk)
                binding.im2.setImageResource(R.drawable.pristavka)
                binding.im3.setImageResource(R.drawable.powerbank)
                count++
            } else{
                binding.im1.setImageResource(R.drawable.chaynik)
                binding.im2.setImageResource(R.drawable.pulesos)
                binding.im3.setImageResource(R.drawable.utug)
                count=0
            }
        }
        //рандом картинок по клику на "Испытать удачу"
        // и списание с баланса 5 баксов + добавление картинки выиграша
        binding.bPlay.setOnClickListener {
            var cash = binding.tvCash.text.toString().toInt()
            if (cash < 5){
                Toast.makeText(this,"Недостаточно денег",Toast.LENGTH_SHORT).show()
                if (binding.tvCash.text == "0") binding.tvCash.setTextColor(Color.RED)
            }else {
                cash -= 5
                binding.tvCash.setText(cash.toString())
                binding.imMain.setImageResource(imageList[random()])

            }
        }

        //по клику открываю фрагмент с пополнением
        binding.bRepenish.setOnClickListener {
            openFrag(R.id.placeHolder,BlankFragment.newInstance())
            binding.placeHolder.visibility = View.VISIBLE
            binding.bRepenish.visibility = View.GONE
        }
        //если баланс ноль, цвет на сумме красный
        if (binding.tvCash.text == "0") binding.tvCash.setTextColor(Color.RED)

        //выход
        binding.bExit.setOnClickListener {
            auth.signOut()
            finish()
        }

    }

    //рандом рандом картинок на розыграше
    private fun random(): Int {
        val imageSize = imageList.size -1
        return (0..imageSize).random()
    }

    //функция для открытия фрагмента
    private fun openFrag(idHolder: Int,f: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(idHolder,f).addToBackStack("key").commit()
    }


}
