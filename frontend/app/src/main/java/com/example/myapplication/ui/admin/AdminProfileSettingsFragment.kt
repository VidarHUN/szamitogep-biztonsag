package com.example.myapplication.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAdminSettingProfileBinding


class AdminProfileSettingsFragment(var userId: String) : Fragment() {

    private var _binding: FragmentAdminSettingProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSettingProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var success: Boolean = true
        var delete: Boolean = false

        //itt lekérem a user jelenlegi adatait a userId segítségével és beállítom, mint hint az edittextekbe vagy utána lehetne
        //nézni hogy akár konkrét szövegként legyen ott bár akkor lehet csak simán text = ...

        //aztán az admin ír az edittext-be

        //a save button-ra kattintva kiveszem az értékeket az edittextből és post-olom a DB-nek
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}