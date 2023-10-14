package com.parrosz.submissiongithubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parrosz.submissiongithubuser.adapter.UserAdapter
import com.parrosz.submissiongithubuser.databinding.FragmentFollowerBinding
import com.parrosz.submissiongithubuser.model.DetailUserModel

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailUserModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(USERNAME)

        setAdapter()

        detailViewModel = ViewModelProvider(
            requireActivity(), ViewModelProvider.NewInstanceFactory()
        )[DetailUserModel::class.java]

        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME)
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

        detailViewModel.followers.observe(viewLifecycleOwner) {
            if (position == 1) {
                adapter.UserList(it)
            }
            showLoading(false)
        }

        detailViewModel.following.observe(viewLifecycleOwner) {
            if (position == 2) {
                adapter.UserList(it)
            }
            showLoading(false)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setAdapter() {
        adapter = UserAdapter()
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}