import java.util.Properties

plugins {
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.compose)
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.androidx)
  implementation(libs.bundles.compose)
  implementation(libs.bundles.koin)
  implementation(libs.bundles.ktor)
  implementation(libs.bundles.coil)
  implementation(libs.bundles.google.maps)

  implementation(libs.swissKnife)

  debugImplementation(libs.compose.ui.tooling)
}

val localPropertiesFile = rootProject.file("local.properties")
if (!localPropertiesFile.exists()) {
  throw IllegalStateException("File \"local.properties\" was not found")
}

val localProperties = Properties()
localPropertiesFile.inputStream().use { localProperties.load(it) }

kotlin {
  jvmToolchain(17)
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

    manifestPlaceholders += "appGoogleMapsSdkKey" to localProperties.getProperty("app.google.maps.key")
  }

  signingConfigs {
    create("development") {
      storeFile = file(localProperties.getProperty("app.signing.development.store.file"))
      storePassword = localProperties.getProperty("app.signing.development.store.password")
      keyAlias = localProperties.getProperty("app.signing.development.key.alias")
      keyPassword = localProperties.getProperty("app.signing.development.key.password")
    }

    create("production") {
      storeFile = file(localProperties.getProperty("app.signing.production.store.file"))
      storePassword = localProperties.getProperty("app.signing.production.store.password")
      keyAlias = localProperties.getProperty("app.signing.production.key.alias")
      keyPassword = localProperties.getProperty("app.signing.production.key.password")
    }
  }

  flavorDimensions += "environment"
  productFlavors {
    create("development") {
      dimension = "environment"
      signingConfig = signingConfigs.getByName("development")
      applicationIdSuffix = ".development"
    }

    create("production") {
      dimension = "environment"
      signingConfig = signingConfigs.getByName("production")
    }
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      signingConfig = null
    }

    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "/META-INF/DEPENDENCIES"
    }
  }
}
