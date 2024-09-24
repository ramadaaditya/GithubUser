package com.dicoding.favorite

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FavModule = module {
    viewModel { FavoriteViewModel(get()) }
}