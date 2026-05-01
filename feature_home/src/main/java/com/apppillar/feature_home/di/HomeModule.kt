package com.apppillar.feature_home.di

import android.content.Context
import androidx.room.Room
import com.apppillar.feature_home.data.local.HomeDatabase
import com.apppillar.feature_home.data.local.dao.DailyStepsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HomeDatabase =
        Room.databaseBuilder(context, HomeDatabase::class.java, "home_db").build()

    @Provides
    fun provideDailyStepsDao(db: HomeDatabase): DailyStepsDao = db.stepDao()

    /*@Provides
    fun provideCompletedWorkoutsDao(db: HomeDatabase): CompletedWorkoutsDao = db.trainingDao()*/

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}