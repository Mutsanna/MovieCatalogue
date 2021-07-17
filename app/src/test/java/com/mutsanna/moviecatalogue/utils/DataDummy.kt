package id.ac.mymoviecatalogue.utils

import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity

object DataDummy {
    fun generateDummyMovie(): MoviesEntity {
        return MoviesEntity(
                460465,
                "Mortal Kombat",
                "2021-04-07",
                "Washed-up MMA fighter Cole Young, unaware of his heritage, and hunted by Emperor Shang Tsung's best warrior, Sub-Zero, seeks out and trains with Earth's greatest champions as he prepares to stand against the enemies of Outworld in a high stakes battle for the universe.",
                "Action, Fantasy, Adventure",
                "Atomic Monster, Broken Road Productions, New Line Cinema",
                "/nkayOAUBUu4mMvyNf9iHSUiPjF1.jpg",
                false
        )
    }

    fun generateDummyTvShow(): TvShowEntity {
        return TvShowEntity(
                60735,
                "The Flash",
                "2014-10-07",
                "After a particle accelerator causes a freak storm, CSI Investigator Barry Allen is struck by lightning and falls into a coma. Months later he awakens with the power of super speed, granting him the ability to move through Central City like an unseen guardian angel. Though initially excited by his newfound powers, Barry is shocked to discover he is not the only \\\"meta-human\\\" who was created in the wake of the accelerator explosion -- and not everyone is using their new powers for good. Barry partners with S.T.A.R. Labs and dedicates his life to protect the innocent. For now, only a few close friends and associates know that Barry is literally the fastest man alive, but it won't be long before the world learns what Barry Allen has become...The Flash.",
                "Warner Bros. Television, Berlanti Productions, DC Entertainment, Mad Ghost Productions",
                "Drama, Sci-Fi & Fantasy",
                "/lJA2RCMfsWoskqlQhXPSLFQGXEJ.jpg",
                false
        )
    }
}