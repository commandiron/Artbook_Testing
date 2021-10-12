package com.commandiron.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.api.RetrofitApi
import com.commandiron.artbooktesting.repo.ArtsRepository
import com.commandiron.artbooktesting.repo.ArtsRepositoryInterface
import com.commandiron.artbooktesting.roomdb.ArtDao
import com.commandiron.artbooktesting.roomdb.ArtDatabase
import com.commandiron.artbooktesting.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, ArtDatabase::class.java, "artdatabase").build()

    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()

    @Singleton
    @Provides
    fun injectRetrofitApi(): RetrofitApi{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api:RetrofitApi) = ArtsRepository(dao, api) as ArtsRepositoryInterface

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )

}