package com.apppillar.feature_exercise_select

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ExerciseSelectModule {

    @Provides
    fun provideExerciseApi(): ExerciseApi =
        Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApi::class.java)

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ExerciseSelectDatabase =
        Room.databaseBuilder(context, ExerciseSelectDatabase::class.java, "exercise_db").build()

    @Provides
    fun provideExerciseDao(db: ExerciseSelectDatabase): ExerciseSelectDao = db.exerciseDao()

    @Provides
    fun provideRepository(
        api: ExerciseApi,
        dao: ExerciseSelectDao
    ): ExerciseSelectRepositoryImpl = ExerciseSelectRepositoryImpl(api, dao)
}