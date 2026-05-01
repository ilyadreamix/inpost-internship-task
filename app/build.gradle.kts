import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  jvmToolchain(17)

  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
  }
}

android {
  compileSdk = libs.versions.app.android.sdk.compile.get().toInt()
  namespace = "io.github.ilyadreamix.inpostinternshiptask"

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  defaultConfig {
    applicationId = "io.github.ilyadreamix.inpostinternshiptask"

    minSdk = libs.versions.app.android.sdk.min.get().toInt()
    targetSdk = libs.versions.app.android.sdk.target.get().toInt()

    versionName = libs.versions.app.version.name.get()
    versionCode = libs.versions.app.version.code.get().toInt()
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      signingConfig = signingConfigs.getByName("debug")
    }

    release {
      isMinifyEnabled = true
      isShrinkResources = true
      signingConfig = signingConfigs.getByName("debug")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    buildFeatures {
      buildConfig = true
      resValues = true
      compose = true
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
        excludes += "/META-INF/DEPENDENCIES"
      }
    }
  }
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.androidx)
  implementation(libs.bundles.compose)
  implementation(libs.bundles.koin)
  implementation(libs.bundles.ktor)
  implementation(libs.bundles.coil)

  debugImplementation(libs.compose.ui.tooling.preview)
}
