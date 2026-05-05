package io.github.ilyadreamix.inpostinternshiptask.shared.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

internal fun Context.permissionGranted(name: String) =
  ActivityCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED
