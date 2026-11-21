package pl.submt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.submt.data.repository.OfflineSubscriptionRepository
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(impl: OfflineSubscriptionRepository): SubscriptionRepository
}
