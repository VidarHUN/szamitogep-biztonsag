package com.example.myapplication.ui.webshop.webshoplistitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentWebshopBinding


class WebShopFragment : Fragment() {

    private var _binding: FragmentWebshopBinding? = null
    private val binding get() = _binding!!
    private lateinit var webShopViewModel: WebShopViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebshopBinding.inflate(inflater, container, false)
        root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webShopViewModel =
            ViewModelProvider(this)[WebShopViewModel::class.java]
        webShopViewModel.setCAFFList(root)

        /*
        val caffListAdapter: ArrayAdapter<String> = ArrayAdapter(
            view.context, android.R.layout.simple_list_item_1, webShopViewModel.caffArray
        )

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if(webShopViewModel.caffArray.contains(query)){
                    caffListAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(webShopViewModel.caffArray.contains(newText)){
                    caffListAdapter.filter.filter(newText)
                }
                return false
            }

        })
        */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}