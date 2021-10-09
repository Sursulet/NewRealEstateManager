package com.sursulet.realestatemanager.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.data.RealEstateDatabase
import com.sursulet.realestatemanager.data.geocoder.GeocoderApi
import com.sursulet.realestatemanager.utils.Constants.BASE_URL
import com.sursulet.realestatemanager.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RealEstateDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideRealEstateDao(db: RealEstateDatabase) = db.realEstateDao()

    @Provides
    fun providePhoto(db: RealEstateDatabase) = db.photoDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(@DefaultDispatcher defaultDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideGeocoderApi(): GeocoderApi =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .build().create(GeocoderApi::class.java)

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_baseline_wallpaper_24)
                .error(R.drawable.ic_baseline_wallpaper_24)
        )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
}