package pl.submt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

/**
 * "Now" and "today" always come from an injected [Clock] instead of `LocalDate.now()`,
 * so date logic can be tested with a fixed date.
 */
@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()
}
