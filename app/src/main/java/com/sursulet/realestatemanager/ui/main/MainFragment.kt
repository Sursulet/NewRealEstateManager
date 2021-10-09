package com.sursulet.realestatemanager.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sursulet.realestatemanager.databinding.MainFragmentBinding
import com.sursulet.realestatemanager.ui.adapters.RealEstateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance(twoPane: Boolean) =
            MainFragment().apply { arguments = Bundle().apply { putBoolean("twoPane", twoPane) } }
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()

    @Inject
    lateinit var list: RealEstateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            val result = it.getBoolean("twoPane")
            viewModel.onEvent(ListEvent.TwoPane(result))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        list.setOnItemClickListener {
            viewModel.onEvent(ListEvent.SelectedId(it))
            setFragmentResult("requestKey", bundleOf("bundleKey" to it))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                list.submitList(it.list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.mainRecyclerview.apply {
            adapter = list
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}