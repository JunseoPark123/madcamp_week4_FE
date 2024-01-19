pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "madcamp_week4_FE"
include(":app")
 