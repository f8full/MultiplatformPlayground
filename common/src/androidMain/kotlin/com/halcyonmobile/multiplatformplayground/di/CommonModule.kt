package com.halcyonmobile.multiplatformplayground.di

import android.content.Context
import com.halcyonmobile.multiplatformplayground.api.ApplicationApi
import com.halcyonmobile.multiplatformplayground.api.CategoryApi
import com.halcyonmobile.multiplatformplayground.api.ScreenshotApi
import com.halcyonmobile.multiplatformplayground.api.db.DatabaseFactory
import com.halcyonmobile.multiplatformplayground.repository.application.ApplicationMemorySource
import com.halcyonmobile.multiplatformplayground.repository.application.ApplicationRemoteSource
import com.halcyonmobile.multiplatformplayground.repository.application.ApplicationRepository
import com.halcyonmobile.multiplatformplayground.repository.category.CategoryLocalSource
import com.halcyonmobile.multiplatformplayground.repository.category.CategoryRemoteSource
import com.halcyonmobile.multiplatformplayground.repository.category.CategoryRepository
import com.halcyonmobile.multiplatformplayground.usecase.*
import org.koin.dsl.module

private fun getApiModule(context: Context) = module {
    factory { ApplicationApi() }
    factory { CategoryApi() }
    factory { ScreenshotApi() }

    single { DatabaseFactory.getInstance(context).create() }
}

private fun getRepositoryModule() = module {
    factory { ApplicationMemorySource() }
    factory { ApplicationRemoteSource(get()) }
    single { ApplicationRepository(get(), get()) }

    factory { CategoryLocalSource(get()) }
    factory { CategoryRemoteSource(get()) }
    single { CategoryRepository(get(), get()) }
}

private fun getUseCaseModule() = module {
    factory { GetCategoriesUseCase(get()) }
    factory { FetchCategoriesUseCase(get()) }
    factory { GetFavouritesUseCase(get()) }
    factory { GetApplicationUseCase(get()) }
    factory { GetApplicationsUseCase(get()) }
    factory { UpdateFavouriteUseCase(get()) }
}

fun getCommonModules(context: Context) =
    listOf(getApiModule(context), getRepositoryModule(), getUseCaseModule())