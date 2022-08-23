package com.wahidabd.dicodingstories.view.page.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.databinding.FragmentRegisterBinding
import com.wahidabd.dicodingstories.utils.lottie.LottieLoading
import com.wahidabd.dicodingstories.utils.mySnackBar
import com.wahidabd.dicodingstories.utils.texTrim
import com.wahidabd.dicodingstories.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    @Inject lateinit var loading: LottieLoading

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvToLogin.setOnClickListener { findNavController().navigateUp() }

        binding.edName.doAfterTextChanged { setMyButton() }
        binding.edEmail.doAfterTextChanged { setMyButton() }
        binding.edPassword.doAfterTextChanged { setMyButton() }
        binding.btnRegister.setOnClickListener { sendRequest() }

        setMyButton()
        playAnimation()
    }

    private fun sendRequest(){
        val name = binding.edName.texTrim()
        val email = binding.edEmail.texTrim()
        val password = binding.edPassword.texTrim()

        val request = RegisterRequest(name, email, password)
        observableViewModel(request)
    }

    private fun observableViewModel(request: RegisterRequest){
        viewModel.register(request).observe(viewLifecycleOwner){res ->
            when(res.status){
                Status.LOADING -> loading.start(requireContext())
                Status.ERROR -> {
                    loading.stop()
                    view?.mySnackBar(res.message.toString())
                }
                Status.SUCCESS -> {
                    loading.stop()
                    view?.mySnackBar(res.data?.message.toString())
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun playAnimation(){
        with(binding){
            val logo = ObjectAnimator.ofFloat(imgLogo, View.ALPHA, 1f).setDuration(500)
            val title = ObjectAnimator.ofFloat(tvTextRegister, View.ALPHA, 1f).setDuration(500)
            val name = ObjectAnimator.ofFloat(linearName, View.ALPHA, 1f).setDuration(500)
            val email = ObjectAnimator.ofFloat(linearEmail, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(linearPassword, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(linearLogin, View.ALPHA, 1f).setDuration(500)
            val register = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(logo, title, name, email, password, login, register)
                start()
            }
        }
    }

    private fun setMyButton(){
        val nameError = binding.edName.error
        val emailError = binding.edEmail.error
        val passwordError = binding.edPassword.error
        val name = binding.edName.texTrim()
        val email = binding.edEmail.texTrim()
        val password = binding.edPassword.texTrim()

        binding.btnRegister.isEnabled = (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
                && (nameError == null && emailError == null && passwordError == null)

    }

}