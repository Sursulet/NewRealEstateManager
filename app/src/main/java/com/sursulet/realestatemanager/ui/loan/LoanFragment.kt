package com.sursulet.realestatemanager.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sursulet.realestatemanager.databinding.LoanFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoanFragment : Fragment() {

    companion object {
        fun newInstance() = LoanFragment()
    }

    private var _binding: LoanFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoanFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            actionCalculate.setOnClickListener { viewModel.onEvent(LoanEvent.Calculate) }
            loanContribution.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(LoanEvent.Contribution(text.toString()))
            }
            loanYears.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(LoanEvent.Years(text.toString()))
            }
            loanRate.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(LoanEvent.Rate(text.toString()))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.apply {
                    loanResult.text = it.result
                    loanContribution.error = it.error.contribution
                    loanYears.error = it.error.years
                    loanRate.error = it.error.rate
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}