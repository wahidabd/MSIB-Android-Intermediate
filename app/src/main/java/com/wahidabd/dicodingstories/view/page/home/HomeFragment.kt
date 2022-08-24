package com.wahidabd.dicodingstories.view.page.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahidabd.dicodingstories.databinding.FragmentHomeBinding
import com.wahidabd.dicodingstories.utils.MySharedPreference
import com.wahidabd.dicodingstories.utils.lottie.LottieLoading
import com.wahidabd.dicodingstories.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by viewModels()
    private lateinit var mAdapter: PostPagingAdapter

    @Inject
    lateinit var loading: LottieLoading

    @Inject
    lateinit var pref: MySharedPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading.start(requireContext())
        mAdapter = PostPagingAdapter(requireContext())

        binding.rvPosts.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        mAdapter.setOnItemClick {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }

        observableViewModel()
    }

    private fun observableViewModel() {
        viewModel.getList().observe(viewLifecycleOwner) { res ->
            mAdapter.submitData(lifecycle, res)
            loading.stop()
        }
    }
}