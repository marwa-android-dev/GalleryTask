package com.atg.gallerytask.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.atg.gallerytask.R
import com.atg.gallerytask.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    private var currentQuery = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearchView()

        searchAdapter = SearchAdapter()
        binding.rvSearchedPhotos.adapter = searchAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchPhotosDataFlow.collect { photos ->
                searchAdapter.submitList(photos)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.networkErrorEvent.collect {
                showRetrySnackBar()
            }
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    currentQuery = newText
                }
                newText?.let { searchViewModel.setSearchQuery(it) }
                return true
            }
        })
    }

    private fun showRetrySnackBar() {
        Snackbar.make(requireView(), getString(R.string.network_error_message), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.retry)) {
            searchViewModel.setSearchQuery(currentQuery)
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}