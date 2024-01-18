package com.bitspanindia.groceryapp.ui.mainFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.AddressesAdapter
import com.bitspanindia.groceryapp.databinding.FragmentAddressListBinding
import com.bitspanindia.groceryapp.model.PlaceModel

class AddressListFragment : Fragment() {
    private lateinit var binding:FragmentAddressListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressListBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddressList()

        binding.btnAddAddress.setOnClickListener {
            val action = AddressListFragmentDirections.actionAddressListFragmentToAddAddressFragment()
            findNavController().navigate(action)
        }

    }

    private fun setAddressList() {
        val list = listOf<PlaceModel>(
            PlaceModel(
                "1",
                "Home",
                "Nai Basti Lodhi Sarai Near Railway Station Sambhal, Uttar Pradesh"
            ),
            PlaceModel(
                "2",
                "Work",
                "Pari Chock Moradabad, Uttar Pradesh"
            ),
            PlaceModel(
                "2",
                "Home",
                "Kerhera Near Mohan Nagar Metro Station, Ghaziabad"
            )
        )

        binding.rvAddresses.adapter = AddressesAdapter(list)
    }

}