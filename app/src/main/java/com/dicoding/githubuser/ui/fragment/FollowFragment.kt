package com.dicoding.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.ui.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.ui.ViewModelFactory

class FollowFragment : Fragment() {
    private lateinit var followViewModel: FollowViewModel
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
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

        setAdapter()

        // Inisialisasi ViewModelFactory
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        followViewModel = ViewModelProvider(this, factory)[FollowViewModel::class.java]

        adapter = ListUserAdapter()
        binding.rvFollow.adapter = adapter


        val username = arguments?.getString(ARG_USERNAME) ?: return

        arguments?.getInt(ARG_POSITION)?.let { position ->
            showLoading(true)
            when (position) {
                1 -> followViewModel.getFollowers(username)
                2 -> followViewModel.getFollowing(username)
                else -> return
            }
        }

        followViewModel.follow.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    adapter.setUserList(result.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
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