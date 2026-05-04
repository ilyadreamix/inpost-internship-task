package io.github.ilyadreamix.inpostinternshiptask.shared.helpers

import android.os.Build
import java.util.Locale

internal fun localeOf(name: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
  Locale.of(name)
} else {
  @Suppress("DEPRECATION") Locale(name)
}
