package com.sursulet.realestatemanager.ui.photo

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.PhotoDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class PhotoDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = PhotoDialogFragment()
    }

    private var _binding: PhotoDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhotoViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            photoDialogToolbar.apply {
                setNavigationOnClickListener { dismiss() }
                title = "Add Photo"
                inflateMenu(R.menu.save_menu)
                isEnabled = false
                setOnMenuItemClickListener {
                    viewModel.onEvent(PhotoEvent.OnSave)
                    return@setOnMenuItemClickListener true
                }
            }

            actionTakeAPhoto.setOnClickListener { takePhoto.launch() }
            actionPickAPhoto.setOnClickListener { getContent.launch("image/*") }

            photoDialogTitle.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(PhotoEvent.Title(text.toString()))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventFlow.collect { event ->
                Toast.makeText(requireContext(), event, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.apply {
                    glide.load(state.image).centerCrop().into(photoImage)
                    if (state.title != photoDialogTitle.editText?.text.toString()) {
                        photoDialogTitle.editText?.setText(state.title)
                    }
                    photoDialogTitle.error = state.errorTitle
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            viewModel.onEvent(PhotoEvent.Image(it))
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val imageStream: InputStream? =
                uri?.let { requireContext().contentResolver.openInputStream(it) }
            imageStream?.let {
                val bitmap = BitmapFactory.decodeStream(it)
                viewModel.onEvent(PhotoEvent.Image(bitmap))
            }
        }

}