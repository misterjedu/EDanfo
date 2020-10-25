package com.misterjedu.edanfo.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

//    @Provides
//    @ActivityScoped
//    fun providePassengerListener(@ActivityContext context: Context) : PassengerRecyclerAdapter.OnPassengerClickListener {
//        return PassengerClickImpl(context)
//    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
