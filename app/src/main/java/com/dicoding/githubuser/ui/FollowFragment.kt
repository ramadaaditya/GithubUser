package com.dicoding.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.viewModel.DetailViewModel

class FollowFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding belum diinisialisasi dengan benar")
    private lateinit var adapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(ARG_USERNAME)

        setAdapter()

        adapter = ListUserAdapter()
        binding.rvFollow.adapter = adapter

        detailViewModel = ViewModelProvider(
            requireActivity(), ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            showLoading(true)
            username?.let {
                detailViewModel.getFollowers(it)
            }
        } else {
            showLoading(true)
            username?.let {
                detailViewModel.getFollowing(it)
            }
        }

        detailViewModel.apply {
            followers.observe(viewLifecycleOwner) {
                if (position == 1) {
                    adapter.setUserList(it)
                }
                showLoading(false)
            }
            following.observe(viewLifecycleOwner) {
                if (position == 2) {
                    adapter.setUserList(it)
                }
                showLoading(false)
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }

    }

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}