plugins {
    id("lib-kotlin-android-no-config")
    id("publish-android")
}

dependencies {
    lintPublish(projects.lifecycleHandlerLint)
}