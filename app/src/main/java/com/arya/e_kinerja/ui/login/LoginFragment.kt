package com.arya.e_kinerja.ui.login

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentLoginBinding
import com.arya.e_kinerja.views.createLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        setUpAction()
    }

    private fun setUpAction() {
        binding.edtUsername.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            binding.inpUsername.error = null
        })

        binding.edtPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            binding.inpPassword.error = null
        })

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            when {
                username.isEmpty() -> {
                    binding.inpUsername.error = resources.getString(R.string.username_kosong)
                }
                password.isEmpty() -> {
                    binding.inpPassword.error = resources.getString(R.string.password_kosong)
                }
                else -> {
                    observePostLogin(username, password)
                }
            }
        }
    }

    private fun observePostLogin(username: String, password: String) {
        viewModel.postLogin(username, password).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.show()
                    }
                    is Result.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(
                            R.id.action_loginFragment_to_dashboardFragment
                        )
                    }
                    is Result.Error -> {
                        loadingDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}