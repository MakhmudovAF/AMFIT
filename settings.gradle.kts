pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://www.jitpack.io" ) }
    }
}

rootProject.name = "AMFIT"
include(":app")
include(":feature_auth")
include(":core")
include(":feature_home")
include(":feature_workout_list")
include(":feature_workout_create")
include(":feature_exercise_select")
include(":feature_workout_edit")
include(":feature_workout_start")
include(":feature_nutrition")
include(":feature_profile")
