package com.wagecalc.app.di

import android.content.Context
import com.wagecalc.app.data.local.AppDatabase
import com.wagecalc.app.data.local.DailyWageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDailyWageDao(database: AppDatabase): DailyWageDao {
        return database.dailyWageDao()
    }
}
