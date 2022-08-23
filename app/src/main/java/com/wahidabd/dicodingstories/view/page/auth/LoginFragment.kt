package com.wahidabd.dicodingstories.view.page.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wahidabd.dicodingstories.utils.MySharedPreference
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.model.UserModel
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.databinding.FragmentLoginBinding
import com.wahidabd.dicodingstories.view.MainActivity
import com.wahidabd.dicodingstories.utils.lottie.LottieLoading
import com.wahidabd.dicodingstories.utils.mySnackBar
import com.wahidabd.dicodingstories.utils.texTrim
import com.wahidabd.dicodingstories.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    @Inject lateinit var prefs: MySharedPreference
    @Inject lateinit var loading: LottieLoading

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvToRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener {
            sendRequest()
        }

        binding.edEmail.doAfterTextChanged { setMyButton() }
        binding.edPassword.doAfterTextChanged { setMyButton() }

        setMyButton()
        playAnimation()
    }

    private fun sendRequest(){
        val email = binding.edEmail.texTrim()
        val password = binding.edPassword.texTrim()

        val request = LoginRequest(email, password)
        observableViewModel(request)
    }

    private fun observableViewModel(request: LoginRequest) {
        viewModel.login(request).observe(viewLifecycleOwner){res ->
            when(res.status){
                Status.LOADING -> loading.start(requireContext())
                Status.ERROR -> {
                    loading.stop()
                    view?.mySnackBar(res.message.toString())
                }
                Status.SUCCESS -> {
                    loading.stop()

                    val user = UserModel(
                        userId = res.data?.loginResult?.userId.toString(),
                        name = res.data?.loginResult?.name.toString(),
                        email = binding.edEmail.texTrim(),
                        token = res.data?.loginResult?.token.toString()
                    )
                    prefs.setUser(user)
                    prefs.setLogin(true)

                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun playAnimation(){
        with(binding){
            val logo = ObjectAnimator.ofFloat(imgLogo, View.ALPHA, 1f).setDuration(500)
            val title = ObjectAnimator.ofFloat(tvTextLogin, View.ALPHA, 1f).setDuration(500)
            val email = ObjectAnimator.ofFloat(linearEmail, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(linearPassword, View.ALPHA, 1f).setDuration(500)
            val register = ObjectAnimator.ofFloat(linearRegister, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)


            AnimatorSet().apply {
                playSequentially(logo, title, email, password, register, login)
                start()
            }
        }
    }

    private fun setMyButton(){
        val emailError = binding.edEmail.error
        val passwordError = binding.edPassword.error
        val email = binding.edEmail.texTrim()
        val password = binding.edPassword.texTrim()

        binding.btnLogin.isEnabled = (email.isNotEmpty() && password.isNotEmpty())
                && (emailError == null && passwordError == null)

    }

}