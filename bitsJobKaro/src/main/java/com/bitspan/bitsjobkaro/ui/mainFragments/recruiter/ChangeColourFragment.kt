package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bitspan.bitsjobkaro.databinding.FragmentChangeColourBinding


class ChangeColourFragment : Fragment() {

    private lateinit var binding: FragmentChangeColourBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentChangeColourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {

        }

    }

    fun color(): List<String> {
        val list = mutableListOf<String>()
        list.add(binding.edit1.text.toString())
        list.add(binding.edit2.text.toString())
        return list
    }

}