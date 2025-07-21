package org.example.project.di

import org.example.project.data.ProgrammingLanguageRepository
import org.example.project.data.ProgrammingLanguageRepositoryImpl
import org.example.project.ui.ProgrammingLanguageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  // Repository
  single<ProgrammingLanguageRepository> { ProgrammingLanguageRepositoryImpl() }

  // ViewModel
  viewModel { ProgrammingLanguageViewModel(get()) }
}
