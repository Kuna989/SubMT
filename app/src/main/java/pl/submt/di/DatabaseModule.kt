package pl.submt.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.submt.data.local.Migrations
import pl.submt.data.local.SubMtDatabase
import pl.submt.data.local.dao.SubscriptionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SubMtDatabase =
        Room.databaseBuilder(context, SubMtDatabase::class.java, SubMtDatabase.NAME)
            .addMigrations(*Migrations.ALL)
            .build()

    @Provides
    fun provideSubscriptionDao(database: SubMtDatabase): SubscriptionDao =
        database.subscriptionDao()
}
