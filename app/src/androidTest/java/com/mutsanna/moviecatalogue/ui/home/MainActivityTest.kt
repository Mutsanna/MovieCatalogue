package com.mutsanna.moviecatalogue.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.data.source.remote.response.MovieDetailResponse
import com.mutsanna.moviecatalogue.data.source.remote.response.TvShowDetailResponse
import com.mutsanna.moviecatalogue.utils.EspressoIdlingResources
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    private val dummyMovie = ApiConfig().getApiService().getListMovies(BuildConfig.MOVIE_API_KEY).execute().body()?.results
    private val sortedDummyMovie = dummyMovie?.sortedBy { it.id }
    private val movieId = sortedDummyMovie!![0].id
    private val detailMovie = ApiConfig().getApiService().getMovieDetail(movieId, BuildConfig.MOVIE_API_KEY).execute().body() as MovieDetailResponse

    private val dummyShow = ApiConfig().getApiService().getListTvShows(BuildConfig.MOVIE_API_KEY).execute().body()?.results
    private val sortedDummyShow = dummyShow?.sortedBy { it.id }
    private val showId = sortedDummyShow!![0].id
    private val detailShow = ApiConfig().getApiService().getTvShowDetail(showId, BuildConfig.MOVIE_API_KEY).execute().body() as TvShowDetailResponse

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource)
    }

    @Test
    fun loadMovies() {
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyMovie!!.size))
    }

    @Test
    fun loadMoviesFavorite() {
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.action_favorite)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Favorite Section")).perform(click())
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
    }

    @Test
    fun loadDetailMovie() {
        val genres = ArrayList<String>()
        val productionCompanies = ArrayList<String>()

        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(withText(detailMovie.title)))
        onView(withId(R.id.tv_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_date)).check(matches(withText(detailMovie.releaseDate)))

        for (genre in detailMovie.genres) {
            genres.add(genre.name)
        }
        onView(withId(R.id.tv_genre)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_genre)).check(matches(withText(genres.toString().replace("[","").replace("]",""))))

        for (production in detailMovie.productionCompanies) {
            productionCompanies.add(production.name)
        }
        onView(withId(R.id.tv_production)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_production)).check(matches(withText(productionCompanies.toString().replace("[","").replace("]",""))))
        onView(withId(R.id.tv_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_overview)).check(matches(withText(detailMovie.overview)))
        onView(withId(R.id.iv_poster)).check(matches(isDisplayed()))
    }

    @Test
    fun loadTvShows() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_show)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyMovie!!.size))
    }

    @Test
    fun loadTvShowsFavorite() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_show)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.action_favorite)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Favorite Section")).perform(click())
    }

    @Test
    fun loadDetailTvShow() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_show)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_production)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_poster)).check(matches(isDisplayed()))
    }
}