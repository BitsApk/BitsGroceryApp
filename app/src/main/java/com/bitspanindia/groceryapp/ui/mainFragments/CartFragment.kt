package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.CartBeforeCheckoutAdapter
import com.bitspanindia.groceryapp.adapter.CartProductAdapter
import com.bitspanindia.groceryapp.databinding.FragmentCartBinding
import com.bitspanindia.groceryapp.datalist.CustomList

class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var mContext:Context
    private lateinit var mActivity:FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)
        binding.tvDelCharge.paintFlags = binding.tvDelCharge.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        binding.btnPay.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToOrderSuccessFragment()
            findNavController().navigate(action)
        }

        setBeforeCheckoutList()
        setCartProductList()
        setInstructionData()

    }

    private fun setCartProductList() {
        binding.rvCartItems.adapter = CartProductAdapter(CustomList(mContext).dataListProduct2)
    }

    private fun setBeforeCheckoutList() {
        binding.rvProducts.adapter = CartBeforeCheckoutAdapter(CustomList(mContext).dataListProduct2)
    }

    private fun setInstructionData(){
        binding.apply {
            avoidCalling.tvInstruction.text = "Avoid calling"
            avoidCalling.ivDontCall.setImageResource(R.drawable.icon_no_call)
            dnotRingBell.tvInstruction.text = "Don't ring the bell"
            dnotRingBell.ivDontCall.setImageResource(R.drawable.icon_notification_off)
            leaveWithGuard.tvInstruction.text = "Leave with guard"
            leaveWithGuard.ivDontCall.setImageResource(R.drawable.icon_guard)
            leaveAtDoor.tvInstruction.text = "Leave at door"
            leaveAtDoor.ivDontCall.setImageResource(R.drawable.icon_door)
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green_500)
//    }

}