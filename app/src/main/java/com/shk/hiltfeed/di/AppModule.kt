package com.shk.hiltfeed.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shk.hiltfeed.BuildConfig
import com.shk.hiltfeed.data.SharedPref
import com.shk.hiltfeed.data.local.NewsFeedDb
import com.shk.hiltfeed.data.local.dao.BlogDao
import com.shk.hiltfeed.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BlogApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FeedApi

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @FeedApi
    fun provideFeedRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.FEED_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @BlogApi
    fun provideBlogRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BLOG_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    internal fun blogApiService(@BlogApi retrofit: Retrofit) = retrofit.create(ApiInterface::class.java)


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPref(sharedPreferences: SharedPreferences): SharedPref = SharedPref(sharedPreferences)

    @Provides
    @Singleton
    internal fun getRoomDb(@ApplicationContext context: Context): NewsFeedDb {
        return Room.databaseBuilder(context, NewsFeedDb::class.java, BuildConfig.LOCAL_DB).build();
    }

    @Provides
    fun provideBlogDao(db: NewsFeedDb): BlogDao = db.blogDao()
}