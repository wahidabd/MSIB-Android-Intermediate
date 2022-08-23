package com.wahidabd.dicodingstories.view.page.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wahidabd.dicodingstories.databinding.FragmentSettingBinding
import com.wahidabd.dicodingstories.utils.MySharedPreference
import com.wahidabd.dicodingstories.view.page.auth.AuthActivity
import com.wahidabd.dicodingstories.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var pref: MySharedPreference
    private val viewModel: ThemeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLanguage.text = Locale.getDefault().displayName
        binding.tvName.text = pref.getUser().name
        binding.tvEmail.text = pref.getUser().email

        binding.linearLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.darkMode.setOnCheckedChangeListener { _, check ->
            viewModel.setTheme(check)
        }

        binding.linearLogout.setOnClickListener {
            logout()
        }

        getTheme()
    }

    private fun logout(){
        pref.logout()
        pref.logout()
        val intent = Intent(activity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun getTheme(){
        viewModel.getTheme().observe(viewLifecycleOwner){
            binding.darkMode.isChecked = it
        }
    }

}