package com.propvivotaskmanagmentapp.propvivoandroid.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.source.FirebaseAuthSource
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.PreferenceDataStoreInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.RegisterUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignInUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object HiltModule {
    @Provides
    fun provideContext(): ApplicationContext{
        return ApplicationContext()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDatabaseReference(
        firebaseAuth: FirebaseAuth
    ): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepositoryInterface {
        return FirebaseAuthSource(auth, firestore)
    }

    @Provides
    @Singleton
    fun providePreferenceDataStoreHelper(
        @ApplicationContext context: Context
    ): PreferenceDataStoreInterface {
        return PreferenceDataStoreHelper(context)
    }


    // UseCases

    // auth use case
    @Provides
    @Singleton
    fun provideRegisterUseCase(
        repository: AuthRepositoryInterface
    ): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(
        repository: AuthRepositoryInterface
    ): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        repository: AuthRepositoryInterface
    ): SignOutUseCase {
        return SignOutUseCase(repository)
    }

    // end auth use cases

}