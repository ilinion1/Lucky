package com.example.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.portfolio.databinding.FragmentBlankBinding

class BlankFragment : Fragment() {
    lateinit var binding: FragmentBlankBinding
    private val dataModel: DataModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlankBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bFragBack.setOnClickListener {
            if (binding.edFrag.text.toString() == "0") {
                binding.edFrag.error = "Введите корректную сумму"
            }else dataModel.message.value = binding.edFrag.text.toString()
            activity?.supportFragmentManager?.popBackStack()
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() = BlankFragment()
    }
}


