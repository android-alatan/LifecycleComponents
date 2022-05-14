plugins {
    id("lib-kotlin-android-no-config")
    id("publish-android")
}

dependencies {
    api(libs.androidx.lifecycle.common)
    api(projects.lifecycleHandlerInvokeadapterApi)
    api(projects.lifecycleHandlerInvokeadapterFlow)
    api(projects.lifecycleHandlerInvokeadapterRx)
    api(libs.test.junit5)
}
