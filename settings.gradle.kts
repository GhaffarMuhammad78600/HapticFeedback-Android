pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        mavenCentral()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //noinspection JcenterRepositoryObsolete
        jcenter()
    }
}

rootProject.name = "HepticFeedBack"
include(":app")
