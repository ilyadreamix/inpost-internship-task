pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage") repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  @Suppress("UnstableApiUsage") repositories {
    mavenLocal()
    maven("https://jitpack.io")
    google()
    mavenCentral()
  }
}

rootProject.name = "inpost-internship-task"

include(":app")
