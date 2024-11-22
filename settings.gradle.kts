pluginManagement {
    repositories {
        google() // Repositório oficial do Android e plugins do Google
        mavenCentral() // Repositório Maven Central
        gradlePluginPortal() // Repositório padrão do Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Falha se houver repositórios em projetos específicos
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-A1"
include(":app")
