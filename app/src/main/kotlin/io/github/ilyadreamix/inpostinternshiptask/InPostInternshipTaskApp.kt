package io.github.ilyadreamix.inpostinternshiptask

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import io.github.ilyadreamix.inpostinternshiptask.data.DataModule
import io.github.ilyadreamix.inpostinternshiptask.data.shared.buildAppHttpClient
import io.github.ilyadreamix.inpostinternshiptask.domain.DomainModule
import io.github.ilyadreamix.inpostinternshiptask.presentation.PresentationModule
import io.github.ilyadreamix.inpostinternshiptask.shared.helpers.localeOf
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.Locale

internal class InPostInternshipTaskApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@InPostInternshipTaskApp)
      modules(AppModule)
    }
  }

  override fun createConfigurationContext(overrideConfiguration: Configuration): Context? {
    Locale.setDefault(LocaleEnglish)
    overrideConfiguration.setLocale(LocaleEnglish)

    return super.createConfigurationContext(overrideConfiguration)
  }

  companion object {
    private val LocaleEnglish = localeOf("en")
  }
}

private val AppModule = module(createdAtStart = @Suppress("KotlinConstantConditions") BuildConfig.DEBUG) {
  single { buildAppHttpClient() }
  includes(DataModule, DomainModule, PresentationModule)
}
