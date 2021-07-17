package com.mutsanna.moviecatalogue.ui.movie

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.data.source.remote.response.ResultsItemMovie
import com.mutsanna.moviecatalogue.databinding.FragmentMovieBinding
import com.mutsanna.moviecatalogue.viewmodel.ViewModelFactory
import com.mutsanna.moviecatalogue.vo.Status

class MovieFragment : Fragment() {
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
            movieAdapter = MovieAdapter()

            viewModel.getMovies().observe(viewLifecycleOwner, { movies ->
                if (movies != null) {
                    when (movies.status) {
                        Status.LOADING -> binding.progbarMovie.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progbarMovie.visibility = View.GONE
                            movieAdapter.submitList(movies.data)
                        }
                        Status.ERROR -> {
                            binding.progbarMovie.visibility = View.GONE
                            Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(binding.rvMovie) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
    }
}