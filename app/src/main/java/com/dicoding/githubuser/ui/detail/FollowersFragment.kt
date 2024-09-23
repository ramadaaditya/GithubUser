package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.data.source.Resource
import com.dicoding.githubuser.databinding.FragmentFollowerBinding
import com.dicoding.githubuser.ui.UsersAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding
    private val detailViewModel: DetailViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = requireActivity().intent.extras?.getString("username").toString()
        binding = FragmentFollowerBinding.bind(view)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollower.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)
        observeData(username)
    }

    private fun observeData(username: String) {
        detailViewModel.getFollowers(username).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    val adapter = UsersAdapter()
                    adapter.submitList(it.data)
                    binding.rvFollower.adapter = adapter
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}