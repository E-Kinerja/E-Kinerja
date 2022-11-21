package com.arya.e_kinerja.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arya.e_kinerja.R
import com.arya.e_kinerja.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()
    private val args: SplashFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeGetSession()
    }

    private fun observeGetSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (session.state == true) {
                    if (args.notifikasi == "") {
                        findNavController().navigate(
                            R.id.action_splashFragment_to_dashboardFragment
                        )
                    } else {
                        findNavController().navigate(
                            R.id.action_splashFragment_to_tugasAktivitasFragment
                        )
                    }
                } else {
                    findNavController().navigate(
                        R.id.action_splashFragment_to_loginFragment
                    )
                }
            }, 1000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}