package com.mutsanna.moviecatalogue.ui.favorite.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.databinding.FragmentMovieBinding
import com.mutsanna.moviecatalogue.ui.movie.MovieAdapter
import com.mutsanna.moviecatalogue.ui.movie.MovieViewModel
import com.mutsanna.moviecatalogue.viewmodel.ViewModelFactory

class MovieFavoriteFragment : Fragment() {
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

            viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favMovies ->
                if (favMovies.isNotEmpty()) {
                    movieAdapter.submitList(favMovies)
                } else {
                    binding.tvMovieError.visibility = View.VISIBLE
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