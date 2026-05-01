package io.github.ilyadreamix.inpostinternshiptask

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.github.ilyadreamix.inpostinternshiptask.data.DataModule
import io.github.ilyadreamix.inpostinternshiptask.domain.DomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class InPostInternshipTaskApp : Application() {
  override fun onCreate() {
    super.onCreate()
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("pl"))

    startKoin {
      androidContext(this@InPostInternshipTaskApp)
      modules(AppModule)
    }
  }
}

private val AppModule = module {
  includes(DataModule, DomainModule)
}
