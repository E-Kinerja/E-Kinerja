package com.arya.e_kinerja.ui.cari_aktivitas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.adapter.AktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentCariAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CariAktivitasFragment : Fragment() {

    private var _binding: FragmentCariAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CariAktivitasViewModel by viewModels()

    private lateinit var aktivitasAdapter: AktivitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCariAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aktivitasAdapter = AktivitasAdapter()
        aktivitasAdapter.onItemClick = {
            findNavController().navigate(
                CariAktivitasFragmentDirections
                    .actionCariAktivitasFragmentToInputAktivitasFragment(it)
            )
        }

        binding.rvAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvAktivitas.setHasFixedSize(true)
        binding.rvAktivitas.adapter = aktivitasAdapter

        binding.edtAktivitas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length >= 3) {
                        aktivitasAdapter.submitList(null)

                        viewModel.postCariAktivitas(p0.toString()).observe(viewLifecycleOwner) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        binding.pb.visibility = View.VISIBLE
                                    }
                                    is Result.Success -> {
                                        binding.pb.visibility = View.GONE
                                        aktivitasAdapter.submitList(result.data)
                                    }
                                    is Result.Error -> {
                                        binding.pb.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}