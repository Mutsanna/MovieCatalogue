package com.mutsanna.moviecatalogue.ui.show

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.databinding.FragmentShowBinding
import com.mutsanna.moviecatalogue.viewmodel.ViewModelFactory
import com.mutsanna.moviecatalogue.vo.Status

class ShowFragment : Fragment() {
    private lateinit var binding: FragmentShowBinding
    private lateinit var viewModel: ShowViewModel
    private lateinit var showAdapter: ShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[ShowViewModel::class.java]
            showAdapter = ShowAdapter()

            viewModel.getShows().observe(viewLifecycleOwner, { shows ->
                if (shows != null) {
                    when (shows.status) {
                        Status.LOADING -> binding.progbarShow.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progbarShow.visibility = View.GONE
                            showAdapter.submitList(shows.data)
                        }
                        Status.ERROR -> {
                            binding.progbarShow.visibility = View.GONE
                            Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(binding.rvShow) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = showAdapter
            }
        }
    }
}