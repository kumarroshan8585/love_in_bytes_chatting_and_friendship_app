pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven { url = uri("https://storage.zego.im/maven") }   // <- Add this line.
        maven { url = uri("https://www.jitpack.io") }

    }
}

rootProject.name = "DatingApp"
include(":app")
 