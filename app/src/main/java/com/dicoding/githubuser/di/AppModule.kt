package com.dicoding.githubuser.di

import com.dicoding.core.domain.usecase.UserInteract
import com.dicoding.core.domain.usecase.UserUseCase
import com.dicoding.githubuser.ui.detail.DetailViewModel
import com.dicoding.githubuser.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> {
        UserInteract(
            get()
        )
    }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}