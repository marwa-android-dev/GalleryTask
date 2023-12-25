package com.atg.gallerytask.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.atg.gallerytask.databinding.FragmentHomeBinding
import com.atg.gallerytask.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PhotoAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photoAdapter = PhotoAdapter()
        photoAdapter.setOnItemClickListener(this)
        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.rvPhotoList.layoutManager = gridLayoutManager

        val footerAdapter = StateAdapter { photoAdapter.retry() }
        binding.rvPhotoList.adapter = photoAdapter.withLoadStateFooter(footerAdapter)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == photoAdapter.itemCount && footerAdapter.itemCount > 0) {
                    ViewType.PHOTO_VIEW_TYPE
                } else {
                    ViewType.NETWORK_VIEW_TYPE
                }.value
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.photosDataFlow
                    .collectLatest { pagingData ->
                        photoAdapter.submitData(pagingData)
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photoTitle: String) {
        Toast.makeText(requireContext(), photoTitle, Toast.LENGTH_LONG).show()
    }
}