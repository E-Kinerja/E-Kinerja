package com.arya.e_kinerja.ui.pengaturan

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arya.e_kinerja.databinding.FragmentPengaturanBinding
import com.arya.e_kinerja.receiver.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PengaturanFragment : Fragment() {

    private var _binding: FragmentPengaturanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PengaturanViewModel by viewModels()

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpAction()
    }

    private fun setUpView() {
        viewModel.getNotifikasi().observe(viewLifecycleOwner) { state ->
            binding.switchNotifikasi.isChecked = state
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpAction() {
        binding.switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            alarmReceiver = AlarmReceiver()

            if (isChecked) {
                alarmReceiver.setReminderAlarm(requireContext())
            } else {
                alarmReceiver.cancelReminderAlarm(requireContext())
            }

            binding.switchNotifikasi.isChecked = isChecked
            viewModel.postNotifikasi(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}