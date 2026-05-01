package io.github.ilyadreamix.inpostinternshiptask

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import io.github.ilyadreamix.inpostinternshiptask.data.DataModule
import io.github.ilyadreamix.inpostinternshiptask.domain.DomainModule
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
    val localePolish = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
      Locale.of("pl")
    } else {
      @Suppress("DEPRECATION") Locale("pl")
    }

    Locale.setDefault(localePolish)
    overrideConfiguration.setLocale(localePolish)

    return super.createConfigurationContext(overrideConfiguration)
  }
}

private val AppModule = module(createdAtStart = BuildConfig.DEBUG) {
  includes(DataModule, DomainModule)
}
